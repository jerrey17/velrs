package com.velrs.engine.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 调度配置
 *
 * @Author rui
 * @Date 2021-08-07 20:50
 **/
@SpringBootConfiguration
@EnableScheduling
public class ScheduleConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(getSchedule());
    }

    @Bean(destroyMethod = "shutdown")
    public ScheduledExecutorService getSchedule() {
        return Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    }
}