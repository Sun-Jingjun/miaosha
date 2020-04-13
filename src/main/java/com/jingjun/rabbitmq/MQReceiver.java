package com.jingjun.rabbitmq;

import com.jingjun.domain.MiaoshaOrder;
import com.jingjun.domain.MiaoshaUser;
import com.jingjun.redis.RedisService;
import com.jingjun.result.CodeMsg;
import com.jingjun.result.Result;
import com.jingjun.service.GoodsService;
import com.jingjun.service.MiaoshaService;
import com.jingjun.service.OrderService;
import com.jingjun.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 */
@Service
public class MQReceiver {

    @Resource
    private GoodsService goodsService;

    @Resource
    private MiaoshaService miaoshaService;

    @Resource
    private OrderService orderService;

    private static Logger logger = LoggerFactory.getLogger(MQReceiver.class);

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receiveMessage(String stringMessage) {
        MiaoshaMessage miaoshaMessage = RedisService.stringToBean(stringMessage, MiaoshaMessage.class);
        MiaoshaUser user = miaoshaMessage.getUser();
        long goodsId = miaoshaMessage.getGoodsId();
        GoodsVo goods = goodsService.getGoodById(goodsId);
        //判断是否秒杀过了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if(order != null) {
            return;
        }
        miaoshaService.miaosha(user,goods);
    }

    @RabbitListener(queues=MQConfig.QUEUE)
    public void receive(String message) {
        logger.info("receive :"+message);
    }

/*    @RabbitListener(queues=MQConfig.TOPIC_QUEUE1)
    public void receiveTopic1(String message) {
        logger.info(" topic  queue1 message:"+message);
    }

    @RabbitListener(queues=MQConfig.TOPIC_QUEUE2)
    public void receiveTopic2(String message) {
        logger.info(" topic  queue2 message:"+message);
    }


    @RabbitListener(queues=MQConfig.HEADER_QUEUE)
    public void receiveHeaderQueue(byte[] message) {
        logger.info(" header  queue message:"+new String(message));
    }*/
}
