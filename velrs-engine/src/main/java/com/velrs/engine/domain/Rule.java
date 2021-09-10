package com.velrs.engine.domain;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 规则表
 *
 * @date 2021-08-08 16:30:45
 */
@Data
@NoArgsConstructor
public class Rule {

    /**
     * 自动递增id
     */
    private Integer id;
    /**
     * 规则id，要求为英文字母，无空格，且全局唯一
     */
    private String ruleId;
    /**
     * 规则中文名称
     */
    private String ruleName;
    /**
     * 规则所属项目ID
     */
    private String projectId;
    /**
     * 规则状态：0:初始（未编辑）;2:编辑中;3:已编译;1:已发布
     */
    private Integer ruleStatus;
    /**
     * 测试状态：0：待测试；1：已测试
     */
    private Integer testStatus;
    /**
     * 编译时间，测试类名会取该时间
     */
    private Long compileTime;
    /**
     * 规则发布状态：0=待发布;1=已发布;
     */
    private Integer publishStatus;
    /**
     * 最后编辑时间
     */
    private Date lastEditTime;
    /**
     * 最后编辑用户
     */
    private Long lastEditUserId;
    /**
     * 最后编译时间
     */
    private Date lastCompileTime;
    /**
     * 最后编译用户
     */
    private Long lastCompileUserId;
    /**
     * 最后一次发布的版本号（发布的时候才累加）
     */
    private Integer lastPublishVersion;
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
    /**
     * 规则描述
     */
    private String description;
    /**
     * 当前正在编辑的规则
     */
    private String rule;
    /**
     * 编译的字节码
     */
    private String ruleClassCode;
    /**
     * 测试请求参数
     */
    private String callParam;

    public boolean isCompile() {
        return ruleStatus == 3;// 3 已编译
    }

}