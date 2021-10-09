package com.velrs.mgt.exception;

import lombok.Getter;

/**
 * @Author rui
 * @Date 2021-08-08 16:25
 **/
@Getter
public class BizException extends RuntimeException {

    private String identityId;

    public BizException(String message) {
        super(message);
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }
}

