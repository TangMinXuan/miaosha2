package com.tmx.miaosha2.controller.VO;

import com.tmx.miaosha2.DAO.DO.GoodsDO;

public class GoodsDetailVO {
    private int miaoshaStatus;
    private int remainSeconds;
    private GoodsDO goods;

    public int getMiaoshaStatus() {
        return miaoshaStatus;
    }

    public void setMiaoshaStatus(int miaoshaStatus) {
        this.miaoshaStatus = miaoshaStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public GoodsDO getGoods() {
        return goods;
    }

    public void setGoods(GoodsDO goods) {
        this.goods = goods;
    }
}
