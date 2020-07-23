package com.tmx.miaosha2.redis.key;

public class MiaoshaOverKey {
    private String key = "MiaoshaOver:goodsId:{%s}";
    private int seconds = 60 * 60 * 24;     //key生存24小时

    public MiaoshaOverKey(Long goodsId) {
        this.key = String.format(this.key, goodsId + "");
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
