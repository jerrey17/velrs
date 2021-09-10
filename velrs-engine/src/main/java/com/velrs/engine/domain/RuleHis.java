package com.velrs.engine.domain;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 规则历史表
 *
 * @date 2021-08-08 16:14:20
 */
@Data
@NoArgsConstructor
public class RuleHis {

    /**
     * PK，自动递增
     */
    private Integer id;
    /**
     * 规则id
     */
    private String ruleId;
    /**
     * 历史版本号
     */
    private Integer version;
    /**
     * 规则内容
     */
    private String rule;
    /**
     * 发布后编译成功的java字节码
     */
    private String ruleClassCode;
    /**
     * 运行时请求参数
     */
    private String callParam;
    /**
     * 编译时间
     */
    private Long compileTime;
    /**
     * 规则创建人id
     */
    private Long createUserId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

}