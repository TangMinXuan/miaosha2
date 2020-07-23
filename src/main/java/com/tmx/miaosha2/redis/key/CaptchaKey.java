package com.tmx.miaosha2.redis.key;

public class CaptchaKey {

    private String key = "Captcha:userId:{%s}";
    private int seconds = 60;     //key生存1分钟

    public CaptchaKey(String userId) {
        this.key = String.format(this.key, userId);
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
