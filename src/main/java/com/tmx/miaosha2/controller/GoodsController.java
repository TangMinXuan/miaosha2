package com.tmx.miaosha2.controller;

import com.tmx.miaosha2.DAO.DO.GoodsDO;
import com.tmx.miaosha2.DAO.DO.UserDO;
import com.tmx.miaosha2.controller.VO.GoodsDetailVO;
import com.tmx.miaosha2.result.ErrorMessage;
import com.tmx.miaosha2.result.GlobalException;
import com.tmx.miaosha2.result.Result;
import com.tmx.miaosha2.service.GoodsService;
import com.tmx.miaosha2.redis.key.CaptchaKey;
import com.tmx.miaosha2.redis.RedisService;
import com.wf.captcha.SpecCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @GetMapping("/list")
    public Result<List<GoodsDO>> getGoodsList(UserDO userDO) {
        if(userDO == null) {
            throw new GlobalException(ErrorMessage.TOKEN_ERROR);
        }
        List<GoodsDO> goodsList = goodsService.getGoodsList();
        return Result.success(goodsList);
    }

    @GetMapping("/detail/{goodsId}")
    public Result<GoodsDetailVO> getGoodsDetail(@PathVariable("goodsId") Long goodsId, UserDO userDO) {
        if(userDO == null) {
            throw new GlobalException(ErrorMessage.TOKEN_ERROR);
        }
        GoodsDetailVO goodsDetailVO = goodsService.getGoodsDetailById(goodsId);
        return Result.success(goodsDetailVO);
    }

    @GetMapping("/captcha")
    public Result<String> getCaptcha(UserDO user) throws IOException {
        if(user == null) {
            throw new GlobalException(ErrorMessage.TOKEN_ERROR);
        }
        SpecCaptcha specCaptcha = new SpecCaptcha(80, 32, 5);
        String verCode = specCaptcha.text().toLowerCase();      //正确的验证码
        System.out.println(verCode);
        CaptchaKey captchaKey = new CaptchaKey(user.getUserId() + "");
        redisService.set(captchaKey.getKey(), verCode, captchaKey.getSeconds());
        return Result.success(specCaptcha.toBase64());
    }
}
