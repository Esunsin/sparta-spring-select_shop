package com.sparta.myselectshop.exception;

public class RestApiException {
    private String msg;
    private int value;

    public RestApiException(String msg, int value) {
        this.msg = msg;
        this.value = value;
    }
}
