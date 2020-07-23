package com.tmx.miaosha2.result;

public class ErrorMessage {

    private int code;
    private String message;

    //通用的错误码
    public static ErrorMessage SUCCESS = new ErrorMessage(0, "success");
    public static ErrorMessage SERVER_ERROR = new ErrorMessage(500100, "服务器异常，请稍后再试");
    public static ErrorMessage BIND_ERROR = new ErrorMessage(500101, "参数检验异常: %s");
    public static ErrorMessage REQUEST_ILLEGAL = new ErrorMessage(500102, "请求非法");
    public static ErrorMessage ACCESS_LIMIT_REACHED= new ErrorMessage(500104, "访问太频繁！");
    //登录模块 5002XX
    public static ErrorMessage TOKEN_ERROR = new ErrorMessage(500210, "未登录或者登录已过期");
    public static ErrorMessage PASSWORD_EMPTY = new ErrorMessage(500211, "登录密码不能为空");
    public static ErrorMessage MOBILE_EMPTY = new ErrorMessage(500212, "手机号不能为空");
    public static ErrorMessage MOBILE_ERROR = new ErrorMessage(500213, "手机号格式错误");
    public static ErrorMessage ACCOUNT_NOT_EXIST = new ErrorMessage(500214, "账号不存在");
    public static ErrorMessage PASSWORD_ERROR = new ErrorMessage(500215, "密码错误");
    //商品模块 5003XX

    //订单模块 5004XX
    public static ErrorMessage ORDER_NOT_EXIST = new ErrorMessage(500400, "订单不存在");

    //秒杀模块 5005XX
    public static ErrorMessage MIAO_SHA_OVER = new ErrorMessage(500500, "商品已经秒杀完毕");
    public static ErrorMessage REPEATE_MIAOSHA = new ErrorMessage(500501, "不能重复秒杀");
    public static ErrorMessage MIAOSHA_FAIL = new ErrorMessage(500502, "秒杀失败");
    public static ErrorMessage CAPTCHA_ERROR = new ErrorMessage(500503, "验证码错误");





    public ErrorMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorMessage fillBindError(Object...args) {
        int code = this.code;
        String message = String.format(this.message, args);
        return new ErrorMessage(code, message);
    }

    @Override
    public String toString() {
        return "ErrorMessage [code=" + code + ", message=" + message + "]";
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
