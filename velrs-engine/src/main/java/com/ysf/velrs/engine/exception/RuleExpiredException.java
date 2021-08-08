package com.ysf.velrs.engine.exception;

/**
 * @Author rui
 * @Date 2021-08-08 16:25
 **/
public class RuleExpiredException extends RuntimeException {

    private String identityId;

    public RuleExpiredException(String message) {
        super(message);
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }
}

