package com.velrs.engine.service.manage;

import com.velrs.engine.config.RuleGovernmentConfig;
import com.velrs.engine.enums.RuleLoaderLevelEnum;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * @Author rui
 * @Date 2021-08-07 20:26
 **/
@Slf4j
public class RunPersistByRedis extends RunGovernment {

    private RuleGovernmentConfig ruleGovernmentConfig;

    public RunPersistByRedis(RuleGovernmentConfig ruleGovernmentConfig) {
        this.ruleGovernmentConfig = ruleGovernmentConfig;
    }

    @Override
    public RunGovernment degrade(GovernmentSummary sum) {
        if (isDealStatus()) {
            return this;
        }
        // 超过了最大异常访问次数，功能降级至db加载
        if (sum.getTimes().get() >= ruleGovernmentConfig.getMaxCallFailTimes()) {
            ruleGovernmentConfig.setRunLoaderLevel(RuleLoaderLevelEnum.DB);
            setAlarmContent(String.format(">>>规则版本更新功能降级至【DB】>>>规则运行时Redis异常访问次数超过阀值%s，实际异常次数%s",
                    ruleGovernmentConfig.getMaxCallFailTimes(), sum.getTimes()));
            sum.reset();
            this.setDealStatus(true);
            this.setAlarm(true);
            log.warn(">>>[服务降级]-运行功能降级至DB");
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
    public RunGovernment upgrade(GovernmentSummary sum) {
        if (isDealStatus()) {
            return this;
        }
        // do nothing...
        return this;
    }

}
