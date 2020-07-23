package com.tmx.miaosha2.controller;

import com.tmx.miaosha2.DAO.DO.GoodsDO;
import com.tmx.miaosha2.DAO.DO.OrderDO;
import com.tmx.miaosha2.DAO.DO.UserDO;
import com.tmx.miaosha2.result.ErrorMessage;
import com.tmx.miaosha2.result.GlobalException;
import com.tmx.miaosha2.result.Result;
import com.tmx.miaosha2.service.GoodsService;
import com.tmx.miaosha2.service.MiaoshaService;
import com.tmx.miaosha2.service.OrderService;
import com.tmx.miaosha2.service.rabbitMQ.MQSender;
import com.tmx.miaosha2.service.rabbitMQ.QueueMessage;
import com.tmx.miaosha2.redis.RedisService;
import com.tmx.miaosha2.redis.key.StockKey;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @Autowired
    OrderService orderService;

    @Autowired
    MQSender sender;

    private Map<Long, Boolean> localOverMap = new HashMap<>();

    //在Redis中生成库存
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsDO> goodsList = goodsService.getGoodsList();
        if(goodsList == null) {
            return ;
        }

        for(GoodsDO goods : goodsList) {
            StockKey key = new StockKey(goods.getGoodsId());
            redisService.set(key.getKey(), goods.getGoodsStock(), key.getSeconds());
            if(goods.getGoodsStock() > 0) {
                localOverMap.put(goods.getGoodsId(), false);
            }
            else {
                localOverMap.put(goods.getGoodsId(), true);
            }
        }
    }

    @GetMapping("/path")
    public Result<String> getMiaoshaPath(@RequestParam("goodsId") Long goodsId,
                                         @RequestParam("verifyCode") String InputVerifyCode,
                                         UserDO user) {
        if(user == null) {
            throw new GlobalException(ErrorMessage.TOKEN_ERROR);
        }
        if(InputVerifyCode == null || !miaoshaService.checkVerifyCode(user, InputVerifyCode)) {
            throw new GlobalException(ErrorMessage.CAPTCHA_ERROR);
        }
        String path = miaoshaService.createPath(user.getUserId(), goodsId);
        return Result.success(path);
    }

    @PostMapping("/do_miaosha/{path}")
    public Result<Integer> doMiaosha(@RequestParam("goodsId") Long goodsId,
                                     @PathVariable("path") String path,
                                     UserDO user) {
        if(user == null) {
            throw new GlobalException(ErrorMessage.TOKEN_ERROR);
        }

        //判断hashmap中的商品库存，减轻redis压力
        if(localOverMap.get(goodsId)) {
            return Result.fail(ErrorMessage.MIAO_SHA_OVER);
        }

        //检查path是否正确
        if(path == null || !miaoshaService.checkPath(user.getUserId(), goodsId, path)) {
            throw new GlobalException(ErrorMessage.REQUEST_ILLEGAL);
        }

        //判断是否重复秒杀
        OrderDO order = orderService.getOrderByUserIdGoodsId(user.getUserId(), goodsId);
        if(order != null) {
            return Result.fail(ErrorMessage.REPEATE_MIAOSHA);
        }

        //预减库存
        StockKey key = new StockKey(goodsId);
        Long stock = redisService.decr(key.getKey());
        if(stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.fail(ErrorMessage.MIAO_SHA_OVER);
        }

        //入队，返回排队中
        QueueMessage queueMessage = new QueueMessage();
        queueMessage.setUserId(user.getUserId());
        queueMessage.setGoodsId(goodsId);
        sender.send(queueMessage);
        return Result.success(0);
    }

    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     * */
    @GetMapping("/result")
    public Result<Long> miaoshaResult(@RequestParam("goodsId") long goodsId,
                                      UserDO user) {
        if(user == null) {
            throw new GlobalException(ErrorMessage.TOKEN_ERROR);
        }
        Long result = miaoshaService.getMiaoshaResult(user.getUserId(), goodsId);
        return Result.success(result);
    }

}
