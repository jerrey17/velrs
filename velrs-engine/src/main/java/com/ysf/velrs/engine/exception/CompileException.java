package com.ysf.velrs.engine.exception;

import lombok.Getter;

/**
 * @Author rui
 * @Date 2021-08-08 16:25
 **/
@Getter
public class CompileException extends RuntimeException {

    private String identityId;

    public CompileException(String message) {
        super(message);
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public CompileException(String message, Throwable cause) {
        super(message, cause);
    }
}

