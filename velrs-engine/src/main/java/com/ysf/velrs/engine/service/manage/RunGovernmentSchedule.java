package com.ysf.velrs.engine.service.manage;

import com.ysf.velrs.engine.service.ruleload.RuleRegistry;
import com.ysf.velrs.engine.service.ruleload.RuleVersionUpgrade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 环境治理
 *
 * @Author rui
 * @Date 2021-08-07 20:52
 **/
@Slf4j
@Component
public class RunGovernmentSchedule {

    @Autowired
    private RunDegradationHandler runDegradationHandler;
    @Autowired
    private RuleRegistry ruleRegistry;
    @Autowired
    private RuleVersionUpgrade ruleVersionUpgrade;

    /**
     * 心跳监控
     */
    @Scheduled(cron = "10 * * * * ?")
    public void heartbeatMonitoring() {
        log.warn(">>>[定时调度]规则运行健康检查...");
        runDegradationHandler.callListener();
    }

    /**
     * 规则升级发现
     */
    @Scheduled(cron = "0 */10 * * * ?")
    public void upgrade() {
        log.warn(">>>[定时调度]规则升级检查...");
        ruleRegistry.getRuleRegistries().forEach((k, v) -> ruleVersionUpgrade.upgrade(v));
    }
}
