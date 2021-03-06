package com.velrs.engine.exception;

import lombok.Getter;

/**
 * @Author rui
 * @Date 2021-07-29 10:33
 **/
@Getter
public class RuleRunnerBizException extends RuntimeException {

    private String identityId;

    public RuleRunnerBizException(String message) {
        super(message);
    }

    public RuleRunnerBizException(String identityId, String message) {
        super(message);
        this.identityId = identityId;
    }

    public RuleRunnerBizException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuleRunnerBizException(String identityId, String message, Throwable cause) {
        super(message, cause);
        this.identityId = identityId;
    }

}