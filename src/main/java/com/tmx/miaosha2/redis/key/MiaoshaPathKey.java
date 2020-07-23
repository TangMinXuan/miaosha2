package com.tmx.miaosha2.redis.key;

public class MiaoshaPathKey {

    private String key = "Path:userId:{%s}:goodsId:{%s}";
    private int seconds = 60;     //key生存1分钟

    public MiaoshaPathKey(Long userId, Long goodsId) {
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
