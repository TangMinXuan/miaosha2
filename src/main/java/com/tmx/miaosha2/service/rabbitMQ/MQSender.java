package com.tmx.miaosha2.service.rabbitMQ;

import com.tmx.miaosha2.redis.RedisService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {

    @Autowired
    AmqpTemplate amqpTemplate;

    @Autowired
    RedisService redisService;

    public void send(QueueMessage message) {
        String msg = redisService.toJSON(message);
        amqpTemplate.convertAndSend(MQConfig.QUEUE_NAME, msg);
    }
}
