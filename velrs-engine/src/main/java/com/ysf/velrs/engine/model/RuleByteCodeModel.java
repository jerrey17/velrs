package com.ysf.velrs.engine.model;

import com.ysf.velrs.engine.domain.RuleByteCode;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 规则字节码表 Model类
 *
 * @date 2021-07-29 10:21:00
 */
@Data
@NoArgsConstructor
public class RuleByteCodeModel {

    /**
     * 主键id
     */
    private Integer id;
    /**
     * 规则id，要求为英文字母，无空格，且全局唯一
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
     * 规则字节码
     */
    private String ruleClassCode;
    /**
     * 字节码大小
     */
    private Integer ruleSize;
    /**
     * 运行时请求参数
     */
    private String callParam;
    /**
     * 编译时间
     */
    private Long compileTime;
    /**
     * 最后一次发布人用户id
     */
    private Long lastPublishUserId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    public RuleByteCodeModel(RuleByteCode domain) {
        this.id = domain.getId();
        this.ruleId = domain.getRuleId();
        this.projectId = domain.getProjectId();
        this.version = domain.getVersion();
        this.ruleClassCode = domain.getRuleClassCode();
        this.ruleSize = domain.getRuleSize();
        this.callParam = domain.getCallParam();
        this.compileTime = domain.getCompileTime();
        this.lastPublishUserId = domain.getLastPublishUserId();
        this.createTime = domain.getCreateTime();
        this.updateTime = domain.getUpdateTime();
    }

    public static RuleByteCode convertDomain(RuleByteCodeModel model) {
        RuleByteCode domain = new RuleByteCode();
        domain.setId(model.getId());
        domain.setRuleId(model.getRuleId());
        domain.setProjectId(model.getProjectId());
        domain.setVersion(model.getVersion());
        domain.setRuleClassCode(model.getRuleClassCode());
        domain.setRuleSize(model.getRuleSize());
        domain.setCallParam(model.getCallParam());
        domain.setCompileTime(model.getCompileTime());
        domain.setLastPublishUserId(model.getLastPublishUserId());
        domain.setCreateTime(model.getCreateTime());
        domain.setUpdateTime(model.getUpdateTime());
        return domain;
    }

}