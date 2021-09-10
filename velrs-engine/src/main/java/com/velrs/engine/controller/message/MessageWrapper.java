package com.velrs.engine.controller.message;

import lombok.Data;

/**
 * @Author rui
 * @Date 2021-08-29 14:07
 **/
@Data
public class MessageWrapper<T> {

    private String code;

    private String msg;

    private T data;

    private int result = 1; // 1:响应成功(default)；2:响应失败

    public MessageWrapper() {
    }

    public MessageWrapper(T data) {
        this.data = data;
        this.result = 1;
    }

    public MessageWrapper(Throwable t) {
        this.code = "000000";
        this.msg = t.getMessage();
        this.result = 2;
    }

    public MessageWrapper(String code, String msg) {
        this.code = code;
        this.msg = msg;
        this.result = 2;
    }
}
