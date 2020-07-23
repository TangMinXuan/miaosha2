package com.tmx.miaosha2.controller;

import com.tmx.miaosha2.DAO.DO.GoodsDO;
import com.tmx.miaosha2.DAO.DO.UserDO;
import com.tmx.miaosha2.controller.VO.LoginVO;
import com.tmx.miaosha2.redis.RedisService;
import com.tmx.miaosha2.redis.key.StockKey;
import com.tmx.miaosha2.result.ErrorMessage;
import com.tmx.miaosha2.result.GlobalException;
import com.tmx.miaosha2.result.Result;
import com.tmx.miaosha2.service.GoodsService;
import com.tmx.miaosha2.service.LoginService;
import com.tmx.miaosha2.util.DBUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    LoginService loginService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @PostMapping("/login")
    public Result<String> login(@Valid LoginVO loginVO,
                                HttpServletResponse response) {
        if(loginVO == null) {
            throw new GlobalException(ErrorMessage.SERVER_ERROR);
        }
        String token = loginService.login(loginVO.getEmail(), loginVO.getPassword(), response);
        return Result.success(token);
    }

    //redis清空、order清空、order自增设置为1，goods库存设置为10
    @GetMapping("/reset")
    public Result<String> reset() throws Exception {
        System.out.println("reset");

        //清空redis，只保留预减库存用的商品
        redisService.delAllKey();
        List<GoodsDO> goodsList = goodsService.getGoodsList();
        for(GoodsDO goods : goodsList) {
            StockKey key = new StockKey(goods.getGoodsId());
            redisService.set(key.getKey(), goods.getGoodsStock(), key.getSeconds());
        }

        Connection conn = DBUtil.getConn();
        String sql =
                "delete from miaosha_order;\n" +
                "ALTER TABLE miaosha_order AUTO_INCREMENT= 1;\n" +
                "update goods set goods_stock = 10;";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.execute();
        pstmt.close();
        conn.close();
        return Result.success("redis、order清空、order自增设置为1，goods库存设置为10");
    }

}
