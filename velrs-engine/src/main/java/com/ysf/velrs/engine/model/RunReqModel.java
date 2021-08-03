package com.ysf.velrs.engine.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 规则执行请求参数
 *
 * @Author rui
 * @Date 2021-07-29 10:28
 **/
@Data
public class RunReqModel implements Serializable {

    private String identityId;

    /**
     * 规则id
     */
    private String ruleId;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 规则版本
     * 如果传了，则执行历史规则，不传，默认执行当前最新规则。
     */
    private Integer version;

    /**
     * fact规则参数
     */
    private Map<String, String> fact;
}
