package com.tmx.miaosha2.controller;

import com.tmx.miaosha2.DAO.DO.GoodsDO;
import com.tmx.miaosha2.DAO.DO.UserDO;
import com.tmx.miaosha2.redis.RedisService;
import com.tmx.miaosha2.redis.key.StockKey;
import com.tmx.miaosha2.result.ErrorMessage;
import com.tmx.miaosha2.result.GlobalException;
import com.tmx.miaosha2.result.Result;
import com.tmx.miaosha2.service.GoodsService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jmeter")
public class JMeterController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    //qps:500 * 100     1678
    @GetMapping("/goods/list/1")
    public Result<List<GoodsDO>> getGoodsList1() {
        List<GoodsDO> goodsList = goodsService.getGoodsList();
        return Result.success(goodsList);
    }

    //qps:500 * 100     2503
    @GetMapping("/goods/list/2")
    public Result<List<GoodsDO>> getGoodsList2() {
        List<GoodsDO> goodsList = redisService.get("goodsList", List.class);
        if (goodsList == null) {
            System.out.println("走数据库");
            goodsList = goodsService.getGoodsList();
            redisService.set("goodsList", goodsList, 3);
        }
        return Result.success(goodsList);
    }

    //qps:500 * 100      2859
    @GetMapping("/goods/list/3")
    public Result<List<GoodsDO>> getGoodsList3() throws InterruptedException {
        List<GoodsDO> goodsList = redisService.get("goodsList", List.class);

        while (goodsList == null) {
            if(redisService.setnx("goodsList-lock", 60)) {
                System.out.println("走数据库");
                goodsList = goodsService.getGoodsList();
                redisService.set("goodsList", goodsList, 3);
                redisService.delKey("goodsList-lock");
                return Result.success(goodsList);
            }
            Thread.sleep(100);
            goodsList = redisService.get("goodsList", List.class);
        }

        return Result.success(goodsList);
    }


}
