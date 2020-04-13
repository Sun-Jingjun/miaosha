package com.jingjun.rabbitmq;

import com.jingjun.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class MQSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    private Logger logger = LoggerFactory.getLogger(MQSender.class);

    public void sendMiaoshaOrder(MiaoshaMessage message) {
        String stringMessage = RedisService.beanToString(message);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE,stringMessage);
        logger.info("miaosha send message :" + stringMessage);
    }

    public void send(Object message) {
        String stringMessage = RedisService.beanToString(message);
        amqpTemplate.convertAndSend(MQConfig.QUEUE,stringMessage);
        logger.info("send :"+stringMessage);
    }

/*    public void sendTopic(Object message) {
		String msg = RedisService.beanToString(message);
		logger.info("send topic message:"+msg);
		amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key1", msg+"1");
		amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", msg+"2");
	}

	public void sendFanout(Object message) {
		String msg = RedisService.beanToString(message);
		logger.info("send fanout message:"+msg);
		amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, "", msg);
	}


	public void sendHeader(Object message) {
		String msg = RedisService.beanToString(message);
		logger.info("send header message:"+msg);
		MessageProperties properties = new MessageProperties();
		properties.setHeader("header1", "value1");
		properties.setHeader("header2", "value2");
		Message obj = new Message(msg.getBytes(), properties);
		amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE, "", obj);
	}*/
}
