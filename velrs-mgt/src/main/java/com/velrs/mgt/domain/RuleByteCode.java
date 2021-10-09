package com.velrs.mgt.domain;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 规则字节码表
 *
 * @date 2021-10-08 14:31:19
 */
@Data
@NoArgsConstructor
public class RuleByteCode {

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

}