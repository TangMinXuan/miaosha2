package com.tmx.miaosha2.result;

public class GlobalException extends RuntimeException {

    private ErrorMessage errorMessage;
    private static final long serialVersionUID = 1L;

    public GlobalException(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
