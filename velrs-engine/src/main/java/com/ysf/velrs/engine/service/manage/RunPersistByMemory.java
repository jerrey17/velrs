package com.ysf.velrs.engine.service.manage;

import com.ysf.velrs.engine.config.RuleGovernmentConfig;
import com.ysf.velrs.engine.enums.RuleLoaderLevelEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

/**
 * @Author rui
 * @Date 2021-08-07 20:24
 **/
@Slf4j
public class RunPersistByMemory extends RunGovernment {


    private RuleGovernmentConfig ruleGovernmentConfig;

    public RunPersistByMemory(RuleGovernmentConfig ruleGovernmentConfig, Supplier<Boolean> supplier) {
        this.ruleGovernmentConfig = ruleGovernmentConfig;
        try {
            this.setBeat(supplier.get());
        } catch (Exception e) {
            this.setBeat(false);
            e.printStackTrace();
        }
    }

    @Override
    public RunGovernment degrade(GovernmentSummary sum) {
        if (isDealStatus()) {
            return this;
        }
        // do nothing...
        return this;
    }

    @Override
    public RunGovernment upgrade(GovernmentSummary sum) {
        if (isDealStatus()) {
            return this;
        }
        if (isBeat()) {
            sum.inc();
        }
        if (sum.getTimes().get() >= ruleGovernmentConfig.getMaxBeatTimes()) {
            ruleGovernmentConfig.setRunLoaderLevel(RuleLoaderLevelEnum.DB);
            setAlarmContent(String.format(">>>规则版本更新功能升级至【DB】>>>规则运行时尝试心跳访问DB次数达到阀值[%s]，实际心跳访问次数[%s]",
                    ruleGovernmentConfig.getMaxCallFailTimes(), sum.getTimes()));
            sum.reset();
            this.setDealStatus(true);
            this.setAlarm(true);
            log.info(">>>[服务还原]-运行功能还原至DB");
        }
        return this;
    }

}
