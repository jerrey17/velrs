package com.ysf.velrs.engine.model;

import lombok.Data;

/**
 * @Author rui
 * @Date 2021-08-07 21:11
 **/
@Data
public class RuleCodeModel {

    /**
     * 规则ID
     */
    private String ruleId;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 是否是最新版本
     */
    private boolean isLatest;

    /**
     * bean Name
     */
    private String beanName;

    /**
     * 规则字节码
     */
    private String ruleClassCode;

}
