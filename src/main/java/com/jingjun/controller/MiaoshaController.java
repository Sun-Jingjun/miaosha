package com.jingjun.controller;

import com.jingjun.domain.MiaoshaOrder;
import com.jingjun.domain.MiaoshaUser;
import com.jingjun.domain.OrderInfo;
import com.jingjun.exception.GlobalException;
import com.jingjun.rabbitmq.MQSender;
import com.jingjun.rabbitmq.MiaoshaMessage;
import com.jingjun.redis.AccessKey;
import com.jingjun.redis.GoodsKey;
import com.jingjun.redis.MiaoshaKey;
import com.jingjun.redis.RedisService;
import com.jingjun.result.CodeMsg;
import com.jingjun.result.Result;
import com.jingjun.service.GoodsService;
import com.jingjun.service.OrderService;
import com.jingjun.service.MiaoshaService;
import com.jingjun.vo.GoodsVo;
import com.sun.org.apache.bcel.internal.classfile.Code;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {

    @Resource
    private GoodsService goodsService;

    @Resource
    private OrderService orderService;

    @Resource
    private MiaoshaService miaoshaService;

    @Resource
    private RedisService redisService;

    @Resource
    private MQSender sender;

    private ConcurrentHashMap<Long, Boolean> localOverMap =  new ConcurrentHashMap<Long, Boolean>();

    /**
     * 系统加载时，进行回调的方法
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goods = goodsService.listAllGoods();
        if(goods == null) {
            return;
        }
        for (GoodsVo good : goods) {
            int stockCount = good.getStockCount();
            redisService.set(GoodsKey.getMiaoshaGoodsStock,""+good.getId(),stockCount);
            if(stockCount <= 0) {
                localOverMap.put(good.getId(),true);
            } else {
                localOverMap.put(good.getId(),false);
            }
        }
    }


    @RequestMapping(value = "/{path}/do_miaosha",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> doMiaosha(MiaoshaUser user, @RequestParam("goodsId")long goodsId, @PathVariable("path")String path) {
        //判断用户是否登录
        if(user == null) {
            return Result.error(CodeMsg.SESSION_NULL);
        }
        //验证path
        boolean check = miaoshaService.checkPath(user, goodsId, path);
        if(!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        //通过内存中的map直接进行判断,如果不符合条件，直接进行返回，避免进行redis查询，减少网络开销
        Boolean over = localOverMap.get(goodsId);
        if(over) {
            return Result.error(CodeMsg.GOODS_NULL);
        }

        //需要不断的去更新map中的状态，这里仅限前面的请求
        int stockCount = redisService.get(GoodsKey.getMiaoshaGoodsStock,""+goodsId,Integer.class);
        if(stockCount <= 0) {
            localOverMap.put(goodsId,true);
        } else {
            localOverMap.put(goodsId,false);
        }
/*        //redis中减库存
        Long result = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
        if(result < 0) {
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.GOODS_NULL);
        }*/
        //避免同一用户重复秒杀
        Long userId = user.getId();
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdAndGoodsId(userId, goodsId);
        if(order != null) {
            return Result.error(CodeMsg.MIAO_SHA_REPEATE);
        }
        //发送消息队列
        MiaoshaMessage miaoshaMessage = new MiaoshaMessage();
        miaoshaMessage.setGoodsId(goodsId);
        miaoshaMessage.setUser(user);
        sender.sendMiaoshaOrder(miaoshaMessage);
        //排队中
        return Result.success(0);
    }


    @RequestMapping(value="/result", method=RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model,MiaoshaUser user,
                                      @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_NULL);
        }
        long result = miaoshaService.getMiaoshaResult(user.getId(), goodsId);
        return Result.success(result);
    }

    @RequestMapping(value="/path", method=RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaPath(HttpServletRequest request, MiaoshaUser user,
                                         @RequestParam("goodsId")long goodsId,
                                         @RequestParam(value="verifyCode", defaultValue="0")int verifyCode
    ) {
        //判断用户是否登录
        if(user == null) {
            return Result.error(CodeMsg.SESSION_NULL);
        }
        //限流操作
        Integer accessCount = redisService.get(AccessKey.getAccessCount,user.getId()+":"+request.getRequestURL(),Integer.class);
        if(accessCount == null) {
            //说明从来没有访问过
            redisService.set(AccessKey.getAccessCount,user.getId()+":"+request.getRequestURL(),1);
        } else if(accessCount > 3){
            //大于三次，则限流
            throw new GlobalException(CodeMsg.PROCESS_TOO_MATCH);
        } else {
            redisService.incr(AccessKey.getAccessCount,user.getId()+":"+request.getRequestURL());
        }

        //检查验证码
        boolean check = miaoshaService.checkVerifyCode(user, goodsId, verifyCode);
        if(!check) {
            return Result.error(CodeMsg.VERIFYCODE_ERROR);
        }
        String path  = miaoshaService.createMiaoshaPath(user, goodsId);
        return Result.success(path);
    }


    @RequestMapping(value="/verifyCode", method=RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaVerifyCod(HttpServletResponse response, MiaoshaUser user,
                                              @RequestParam("goodsId")long goodsId) {
        if(user == null) {
            return Result.error(CodeMsg.SESSION_NULL);
        }
        try {
            BufferedImage image  = miaoshaService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        }catch(Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }
    }
}
