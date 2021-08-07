package com.ysf.velrs.engine.service.manage;

import com.ysf.velrs.engine.config.RuleGovernmentConfig;
import com.ysf.velrs.engine.service.data.RuleByteCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

/**
 * 规则治理控制器
 *
 * @Author rui
 * @Date 2021-08-07 20:36
 **/
@Slf4j
@Component
public class RunDegradationHandler {

    private final GovernmentSummary callFailSum = new GovernmentSummary();
    private final GovernmentSummary heartBeatSum = new GovernmentSummary();

    @Autowired
    private RuleGovernmentConfig ruleGovernmentConfig;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RuleByteCodeService ruleByteCodeService;

    /**
     * 访问异常次数累加1
     */
    public void incFail() {
        int count = callFailSum.inc();
        log.info(">>>从[{}]起始，异常访问次数累计[{}]次", callFailSum.getBeginTime().format(DateTimeFormatter.ISO_DATE_TIME), count);
    }

    /**
     * 规则运行调用监测
     */
    public void callListener() {
        RunGovernment runGovernment;
        switch (ruleGovernmentConfig.getRunLoaderLevel()) {
            case REDIS:
                runGovernment = new RunPersistByRedis(ruleGovernmentConfig);
                break;
            case DB:
                runGovernment = new RunPersistByDB(ruleGovernmentConfig, () -> {
                    stringRedisTemplate.opsForValue().get("X");
                    return true;
                });
                break;
            case MEMORY:
            default:
                runGovernment = new RunPersistByMemory(ruleGovernmentConfig, () -> {
                    ruleByteCodeService.checkDbIsOK();
                    return true;
                });
        }
        runGovernment
                .degrade(callFailSum)
                .upgrade(heartBeatSum)
                .alarm((data) -> {
                    // data：告警内容。
                    // todo 根据公司实际告警组件假如告警代码，比如邮件告警、企业微信告警、短信告警等方式。
                });
    }
}
