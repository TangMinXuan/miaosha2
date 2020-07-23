package com.tmx.miaosha2.result;

import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<String> handler(Exception e) {

        System.out.println("进入全局异常处理器");

        //异常是已经定义好的异常
        if (e instanceof GlobalException) {
            GlobalException ge = (GlobalException) e;
            System.out.println(ge.getErrorMessage().getMessage());
            return Result.fail(ge.getErrorMessage());
        }
        //异常是由validated产生的BindException
        else if (e instanceof BindException) {
            BindException be = (BindException) e;
            List<ObjectError> errors = be.getAllErrors();
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            System.out.println("msg = " + msg);
            return Result.fail(ErrorMessage.BIND_ERROR.fillBindError(msg));
        }
        //否则统一返回 服务器错误 ，对客户端隐藏错误信息
        else {
            return Result.fail(ErrorMessage.SERVER_ERROR);
        }
    }
}
