package com.ysf.velrs.engine.service.manage;

import lombok.Data;

import java.util.function.Consumer;

/**
 * 规则运行治理
 *
 * @Author rui
 * @Date 2021-08-07 20:02
 **/
@Data
public abstract class RunGovernment {
    /**
     * 是否已被处理：默认：否
     */
    private boolean isDealStatus = false;

    /**
     * 是否存在心跳，默认：有
     */
    private boolean isBeat = true;

    /**
     * 是否告警，默认：false
     */
    private boolean isAlarm = false;

    /**
     * 告警内容
     */
    private String alarmContent;

    /**
     * 功能降级
     *
     * @param sum
     * @return
     */
    abstract RunGovernment degrade(GovernmentSummary sum);

    /**
     * 功能升级
     *
     * @param sum
     * @return
     */
    abstract RunGovernment upgrade(GovernmentSummary sum);

    /**
     * 告警
     *
     * @return
     */
    public RunGovernment alarm(Consumer<String> consumer) {
        if (isAlarm()) {
            consumer.accept(getAlarmContent());
        }
        return this;
    }
}
