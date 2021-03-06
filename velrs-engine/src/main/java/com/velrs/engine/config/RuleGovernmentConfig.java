package com.velrs.engine.config;

import com.velrs.engine.enums.RuleLoaderLevelEnum;
import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 规则治理配置
 *
 * @Author rui
 * @Date 2021-08-07 20:05
 **/
@Data
@SpringBootConfiguration
@ConfigurationProperties(prefix = "velrs.government")
public class RuleGovernmentConfig {

    /**
     * 规则运行加载级别（redis(default)/db/memory）
     */
    private RuleLoaderLevelEnum runLoaderLevel = RuleLoaderLevelEnum.REDIS;

    /**
     * 最大的调用失败次数
     */
    private int maxCallFailTimes = 6;

    /**
     * 最大心跳次数
     */
    private int maxBeatTimes = 6;

    /**
     * gc回收步长（1-60min），默认5，单位：分钟
     */
    private int gcStep = 5;
}
