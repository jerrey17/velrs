package com.velrs.engine.service.manage;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 统计
 *
 * @Author rui
 * @Date 2021-08-07 20:03
 **/
@Data
public class GovernmentSummary {

    private AtomicInteger times = new AtomicInteger();

    private LocalDateTime beginTime = LocalDateTime.now();

    /**
     * 累加1
     */
    public int inc() {
        return times.incrementAndGet();
    }

    /**
     * 重置
     */
    public void reset() {
        setTimes(new AtomicInteger());
        setBeginTime(LocalDateTime.now());
    }
}
