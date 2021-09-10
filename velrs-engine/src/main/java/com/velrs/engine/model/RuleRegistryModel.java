package com.velrs.engine.model;

import com.velrs.engine.constant.RuleRunnerConstant;
import lombok.Data;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 规则注册表
 *
 * @Author rui
 * @Date 2021-08-07 21:00
 **/
@Data
public class RuleRegistryModel {

    /**
     * 规则id
     */
    private String ruleId;

    /**
     * 当前注册的规则版本
     */
    private Integer version;

    /**
     * 是否是最新的版本，true：最新版本；false：历史版本（默认）
     */
    private boolean isLatest = false;

    /**
     * 规则bean名称
     */
    private String ruleBeanName;

    /**
     * 上一次回收时统计的次数
     */
    private int lastCallTimes;

    /**
     * 调用次数
     */
    private AtomicInteger callTimes = new AtomicInteger(RuleRunnerConstant.ZERO);

    /**
     * 权重
     */
    private AtomicInteger weight = new AtomicInteger(RuleRunnerConstant.ZERO);

    /**
     * 创建时间
     */
    private Date createTime = new Date();

    public RuleRegistryModel(String ruleId, Integer version, boolean isLatest, String ruleBeanName) {
        this.ruleId = ruleId;
        this.version = version;
        this.isLatest = isLatest;
        this.ruleBeanName = ruleBeanName;
    }

    public RuleRegistryModel(String ruleId, Integer version, String ruleBeanName) {
        this.ruleId = ruleId;
        this.version = version;
        this.ruleBeanName = ruleBeanName;
    }
}
