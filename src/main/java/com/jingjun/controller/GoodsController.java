package com.jingjun.controller;

import com.jingjun.domain.MiaoshaUser;
import com.jingjun.domain.User;
import com.jingjun.redis.GoodsKey;
import com.jingjun.redis.MiaoShaUserKey;
import com.jingjun.redis.RedisService;
import com.jingjun.result.Result;
import com.jingjun.service.GoodsService;
import com.jingjun.service.MiaoShaUserService;
import com.jingjun.vo.GoodsDetailVo;
import com.jingjun.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Controller
@RequestMapping("/goods")
public class
GoodsController {

    private Integer count = new Integer(0);

    @Resource
    private MiaoShaUserService miaoShaUserService;

    @Resource
    private GoodsService goodsService;

    @Resource
    private RedisService redisService;

    @Resource
    private ThymeleafViewResolver thymeleafViewResolver;

    @Resource
    private ApplicationContext applicationContext;

    @RequestMapping(value = "/to_list",produces = "text/html")
    @ResponseBody
    public String list(HttpServletRequest request,HttpServletResponse response,Model model, MiaoshaUser user) {
        //查询缓存，如果有，直接返回
        String html = redisService.get(GoodsKey.getGoodsList,"",String.class);
        if(!StringUtils.isEmpty(html)) {
            System.out.println(count++);
            return html;
        }
        model.addAttribute("user",user);
        List<GoodsVo> goodsVos = goodsService.listAllGoods();
        model.addAttribute("goodsList",goodsVos);
        //手动解析,加入缓存
        SpringWebContext springWebContext = new SpringWebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", springWebContext);
        if(!StringUtils.isEmpty(html)) {
            System.out.println("更新商品列表页缓存");
            redisService.set(GoodsKey.getGoodsList,"",html);
        }
        return html;
    }

    /**
     * 页面缓存，优化商品详情页
     * @param request
     * @param response
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("/to_detail2/{goodsId}")
    @ResponseBody
    public String goodList2(HttpServletRequest request,HttpServletResponse response,Model model, MiaoshaUser user, @PathVariable("goodsId")long goodsId) {
        model.addAttribute("user",user);
        //查询缓存，如果有，直接返回
        String html = redisService.get(GoodsKey.getGoodsDetail,""+goodsId,String.class);
        if(!StringUtils.isEmpty(html)) {
            return html;
        }
        GoodsVo goodById = goodsService.getGoodById(goodsId);
        //获取秒杀开始时间，秒杀结束时间，以及服务器当前时间
        long startTime = goodById.getStartDate().getTime();
        long endTime = goodById.getEndDate().getTime();
        long nowTime = System.currentTimeMillis();


        //定义秒杀状态码：0表示未开始 1表示进行中 2表示已经结束
        int status = 0;

        //定义秒杀剩余时间
        long remainTime = 0;

        if(nowTime < startTime) {
            //秒杀未开始
            status = 0;
            remainTime = (startTime-nowTime)/1000;
        } else if(nowTime < endTime) {
            //秒杀正在进行中
            status = 1;
            remainTime = 0;
        } else {
            //秒杀已经结束
            status = 2;
            remainTime = -1;
        }

        model.addAttribute("miaoshaStatus",status);
        model.addAttribute("remainSeconds",remainTime);
        model.addAttribute("goods",goodById);
        //手动解析,加入缓存
        SpringWebContext springWebContext = new SpringWebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", springWebContext);
        if(!StringUtils.isEmpty(html)) {
            System.out.println("更新商品详情缓存，商品id:"+goodsId);
            redisService.set(GoodsKey.getGoodsDetail,""+goodsId,html);
        }
        return html;
    }

    /**
     * 页面静态化
     * @param request
     * @param response
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> goodList(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user, @PathVariable("goodsId")long goodsId) {
        GoodsVo goodById = goodsService.getGoodById(goodsId);
        //获取秒杀开始时间，秒杀结束时间，以及服务器当前时间
        long startTime = goodById.getStartDate().getTime();
        long endTime = goodById.getEndDate().getTime();
        long nowTime = System.currentTimeMillis();

        //定义秒杀状态码：0表示未开始 1表示进行中 2表示已经结束
        int status = 0;
        //定义秒杀剩余时间
        int remainTime = 0;

        if(nowTime < startTime) {
            //秒杀未开始
            status = 0;
            remainTime = (int)(startTime-nowTime)/1000;
        } else if(nowTime < endTime) {
            //秒杀正在进行中
            status = 1;
            remainTime = 0;
        } else {
            //秒杀已经结束
            status = 2;
            remainTime = -1;
        }
        GoodsDetailVo detail = new GoodsDetailVo();
        detail.setUser(user);
        detail.setMiaoshaStatus(status);
        detail.setRemainSeconds(remainTime);
        detail.setGoods(goodById);
        return Result.success(detail);
    }
}
