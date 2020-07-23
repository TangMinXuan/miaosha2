package com.tmx.miaosha2.service;

import com.tmx.miaosha2.DAO.DO.OrderDO;
import com.tmx.miaosha2.DAO.DO.UserDO;
import com.tmx.miaosha2.result.ErrorMessage;
import com.tmx.miaosha2.result.GlobalException;
import com.tmx.miaosha2.redis.*;
import com.tmx.miaosha2.redis.key.CaptchaKey;
import com.tmx.miaosha2.redis.key.MiaoshaOverKey;
import com.tmx.miaosha2.redis.key.MiaoshaPathKey;
import com.tmx.miaosha2.redis.key.OrderKey;
import com.tmx.miaosha2.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MiaoshaService {

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    public boolean checkVerifyCode(UserDO user, String InputVerifyCode) {
        CaptchaKey captchaKey = new CaptchaKey(user.getUserId() + "");
        String rightCode = redisService.get(captchaKey.getKey(), String.class);
        return rightCode.equals(InputVerifyCode);
    }

    public String createPath(Long userId, Long goodsId) {
        String path = UUIDUtil.getUUID();
        MiaoshaPathKey key = new MiaoshaPathKey(userId, goodsId);
        redisService.set(key.getKey(), path, key.getSeconds());
        return path;
    }

    public boolean checkPath(Long userId, Long goodsId, String inputPath) {
        MiaoshaPathKey key = new MiaoshaPathKey(userId, goodsId);
        String rightPath = redisService.get(key.getKey(), String.class);
        return rightPath.equals(inputPath);
    }

    //扣库存，生成订单，订单写入redis
    @Transactional
    public OrderDO doMiaosha(Long userId, Long goodsId) {
        if(goodsService.reduceStock(goodsId) != 1) {
            MiaoshaOverKey key = new MiaoshaOverKey(goodsId);
            //内存标记。标记哪些goods秒杀完了，用于回应轮询
            redisService.set(key.getKey(), goodsId, key.getSeconds());
            throw new GlobalException(ErrorMessage.MIAOSHA_FAIL);
        }
        OrderDO order = orderService.createOrder(userId, goodsId);
        OrderKey key = new OrderKey(userId, goodsId);
        redisService.set(key.getKey(), order, key.getSeconds());
        return order;
    }

    //回应客户端轮询的方法
    public Long getMiaoshaResult(Long userId, Long goodsId) {
        OrderDO order = orderService.getOrderByUserIdGoodsId(userId, goodsId);
        //秒杀成功，返回orderId
        if(order != null) {
            return order.getOrderId();
        }
        else {
            MiaoshaOverKey key = new MiaoshaOverKey(goodsId);
            boolean isOver = redisService.exists(key.getKey()); //获取内存标记
            //秒杀失败
            if(isOver) {
                return -1L;
            }
            //请求还在排队
            else {
                return 0L;
            }
        }
    }
}
