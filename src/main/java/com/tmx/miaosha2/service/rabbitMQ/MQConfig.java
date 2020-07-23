package com.tmx.miaosha2.service.rabbitMQ;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    static final String QUEUE_NAME = "direct_queue";

    @Bean
    public Queue getDirectQueue() {
        return new Queue(QUEUE_NAME, true);
    }
}
