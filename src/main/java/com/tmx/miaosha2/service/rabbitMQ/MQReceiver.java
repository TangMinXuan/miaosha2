package com.tmx.miaosha2.service.rabbitMQ;

import com.tmx.miaosha2.DAO.DO.GoodsDO;
import com.tmx.miaosha2.DAO.DO.OrderDO;
import com.tmx.miaosha2.redis.key.MiaoshaOverKey;
import com.tmx.miaosha2.service.GoodsService;
import com.tmx.miaosha2.service.MiaoshaService;
import com.tmx.miaosha2.service.OrderService;
import com.tmx.miaosha2.redis.RedisService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @RabbitListener(queues = MQConfig.QUEUE_NAME)
    public void receive(String message) {
        QueueMessage queueMessage = redisService.fromJSON(message, QueueMessage.class);
        Long userId = queueMessage.getUserId();
        Long goodsId = queueMessage.getGoodsId();

        //判断库存
        MiaoshaOverKey key = new MiaoshaOverKey(goodsId);
        boolean isOver = redisService.exists(key.getKey()); //获取内存标记
        if(isOver) {
            return ;
        }

        //判断是否重复购买
        OrderDO order = orderService.getOrderByUserIdGoodsId(userId, goodsId);
        if(order != null) {
            return ;
        }

        //减库存 生成订单 等待客户端轮询
        miaoshaService.doMiaosha(userId, goodsId);
    }
}
