package com.tmx.miaosha2.service;

import com.tmx.miaosha2.DAO.DO.GoodsDO;
import com.tmx.miaosha2.DAO.GoodsDAO;
import com.tmx.miaosha2.controller.VO.GoodsDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    GoodsDAO goodsDAO;

    public List<GoodsDO> getGoodsList() {
        return goodsDAO.getGoodsList();
    }

    public GoodsDetailVO getGoodsDetailById(Long goodsId) {
        GoodsDO goodsDO = goodsDAO.getGoodsById(goodsId);
        long startAt = goodsDO.getGoodsStartTime().getTime();
        long endAt = goodsDO.getGoodsEndTime().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;

        //秒杀还没开始，倒计时
        if(now < startAt) {
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }
        //秒杀已经结束
        else  if(now > endAt) {
            miaoshaStatus = 2;
            remainSeconds = -1;
        }
        //秒杀进行中
        else {
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVO goodsDetailVO = new GoodsDetailVO();
        goodsDetailVO.setGoods(goodsDO);
        goodsDetailVO.setMiaoshaStatus(miaoshaStatus);
        goodsDetailVO.setRemainSeconds(remainSeconds);
        return goodsDetailVO;
    }

    public GoodsDO getGoodsById(Long goodsId) {
        return goodsDAO.getGoodsById(goodsId);
    }

    public int reduceStock(long goodsId) {
        return goodsDAO.reduceStock(goodsId);
    }
}
