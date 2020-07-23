package com.tmx.miaosha2.DAO.DO;

import java.util.Date;

public class GoodsDO {

    private Long goodsId;
    private String goodsName;
    private String goodsImg;
    private double goodsOriginalPrice;
    private double goodsMiaoshaPrice;
    private int goodsStock;
    private Date goodsStartTime;
    private Date goodsEndTime;

    @Override
    public String toString() {
        return "GoodsDO{" +
                "goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsImg='" + goodsImg + '\'' +
                ", goodsOriginalPrice=" + goodsOriginalPrice +
                ", goodsMiaoshaPrice=" + goodsMiaoshaPrice +
                ", goodsStock=" + goodsStock +
                ", goodsStartTime=" + goodsStartTime +
                ", goodsEndTime=" + goodsEndTime +
                '}';
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public double getGoodsOriginalPrice() {
        return goodsOriginalPrice;
    }

    public void setGoodsOriginalPrice(double goodsOriginalPrice) {
        this.goodsOriginalPrice = goodsOriginalPrice;
    }

    public double getGoodsMiaoshaPrice() {
        return goodsMiaoshaPrice;
    }

    public void setGoodsMiaoshaPrice(double goodsMiaoshaPrice) {
        this.goodsMiaoshaPrice = goodsMiaoshaPrice;
    }

    public int getGoodsStock() {
        return goodsStock;
    }

    public void setGoodsStock(int goodsStock) {
        this.goodsStock = goodsStock;
    }

    public Date getGoodsStartTime() {
        return goodsStartTime;
    }

    public void setGoodsStartTime(Date goodsStartTime) {
        this.goodsStartTime = goodsStartTime;
    }

    public Date getGoodsEndTime() {
        return goodsEndTime;
    }

    public void setGoodsEndTime(Date goodsEndTime) {
        this.goodsEndTime = goodsEndTime;
    }
}
