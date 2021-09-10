package com.velrs.engine.service.manage;

import com.velrs.engine.config.RuleGovernmentConfig;
import com.velrs.engine.enums.RuleLoaderLevelEnum;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.function.Supplier;

/**
 * @Author rui
 * @Date 2021-08-07 20:04
 **/
@Slf4j
public class RunPersistByDB extends RunGovernment {

    private RuleGovernmentConfig ruleGovernmentConfig;

    public RunPersistByDB(RuleGovernmentConfig ruleGovernmentConfig, Supplier<Boolean> supplier) {
        this.ruleGovernmentConfig = ruleGovernmentConfig;
        try {
            this.setBeat(supplier.get());
        } catch (Exception e) {
            this.setBeat(false);
            e.printStackTrace();
        }
    }

    @Override
    RunGovernment degrade(GovernmentSummary sum) {
        if (isDealStatus()) {
            return this;
        }
        // 超过了最大异常访问次数，功能降级至memory加载
        if (sum.getTimes().get() >= ruleGovernmentConfig.getMaxCallFailTimes()) {
            ruleGovernmentConfig.setRunLoaderLevel(RuleLoaderLevelEnum.MEMORY);
            setAlarmContent(String.format(">>>规则版本更新功能降级至【Memory】>>>规则运行时DB异常访问次数超过阀值%s，实际异常次数%s",
                    ruleGovernmentConfig.getMaxCallFailTimes(), sum.getTimes()));
            sum.reset();
            this.setDealStatus(true);
            this.setAlarm(true);
            log.info(">>>[服务降级]-运行功能降级至Memory");
        } else {
            if (sum.getTimes().get() == 0) {
                return this;
            }
            // 容错性：允许在一段时间之内的异常在阀值之内
            LocalDateTime now = LocalDateTime.now().minusHours(1);
            if (sum.getBeginTime().isBefore(now)) {
                sum.reset();
            }
        }
        return this;
    }

    @Override
    RunGovernment upgrade(GovernmentSummary sum) {
        if (isDealStatus()) {
            return this;
        }
        if (isBeat()) {
            sum.inc();
        }
        if (sum.getTimes().get() >= ruleGovernmentConfig.getMaxBeatTimes()) {
            ruleGovernmentConfig.setRunLoaderLevel(RuleLoaderLevelEnum.REDIS);
            setAlarmContent(String.format(">>>规则版本更新功能升级至【Redis】>>>规则运行时尝试心跳访问Redis次数达到阀值[%s]，实际心跳访问次数[%s]",
                    ruleGovernmentConfig.getMaxCallFailTimes(), sum.getTimes()));
            sum.reset();
            this.setDealStatus(true);
            this.setAlarm(true);
            log.info(">>>[服务还原]-运行功能还原至Redis");
        }
        return this;
    }
}
