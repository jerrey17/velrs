package com.ysf.velrs.engine.exception;

import lombok.Getter;

/**
 * @Author rui
 * @Date 2021-08-08 16:25
 **/
@Getter
public class RuleExpiredException extends RuntimeException {

    private String identityId;

    public RuleExpiredException(String message) {
        super(message);
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }
}

