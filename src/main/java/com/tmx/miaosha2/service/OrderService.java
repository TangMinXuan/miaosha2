package com.tmx.miaosha2.service;

import com.tmx.miaosha2.DAO.DO.OrderDO;
import com.tmx.miaosha2.DAO.OrderDAO;
import com.tmx.miaosha2.result.ErrorMessage;
import com.tmx.miaosha2.result.GlobalException;
import com.tmx.miaosha2.redis.key.OrderKey;
import com.tmx.miaosha2.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    RedisService redisService;

    @Autowired
    OrderDAO orderDAO;

    //用于 判断是否重复秒杀 和 回应客户端的轮询
    // 只查redis
    public OrderDO getOrderByUserIdGoodsId(Long userId, Long goodsId) {
        OrderKey key = new OrderKey(userId, goodsId);
        OrderDO order = redisService.get(key.getKey(), OrderDO.class);
        return order;
    }

    //生成订单
    public OrderDO createOrder(long userId, long goodsId) {
        if(orderDAO.createOrder(userId, goodsId) != 1) {
            throw new GlobalException(ErrorMessage.REPEATE_MIAOSHA);
        }
        return orderDAO.getOderByUserIdGoodsId(userId, goodsId);
    }

    public OrderDO getOrderById(Long orderId) {
        return orderDAO.getOrderById(orderId);
    }
}
