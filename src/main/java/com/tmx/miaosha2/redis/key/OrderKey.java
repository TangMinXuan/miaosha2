package com.tmx.miaosha2.redis.key;

public class OrderKey {
    private String key = "Order:userId:{%s}:goodsId:{%s}";
    private int seconds = 60 * 60;     //key生存1h

    public OrderKey(Long userId, Long goodsId) {
        this.key = String.format(this.key, userId + "", goodsId + "");
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
