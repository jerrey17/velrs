package com.ysf.velrs.engine.service.runner;

import com.alibaba.fastjson.JSON;
import com.ysf.velrs.engine.config.RuleGovernmentConfig;
import com.ysf.velrs.engine.domain.RuleByteCode;
import com.ysf.velrs.engine.model.RuleRegistryModel;
import com.ysf.velrs.engine.service.RuleBuilder;
import com.ysf.velrs.engine.service.data.RuleByteCodeService;
import com.ysf.velrs.engine.service.manage.RunDegradationHandler;
import com.ysf.velrs.engine.utils.RuleVersionUtil;
import io.lettuce.core.RedisException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.redis.connection.PoolException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 规则版本升级管控
 *
 * @Author rui
 * @Date 2021-08-08 10:54
 **/
@Slf4j
@Component
public class RuleVersionUpgrade {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RulerBeanLoader rulerBeanLoader;
    @Autowired
    private RuleGovernmentConfig ruleGovernmentConfig;
    @Autowired
    private RunDegradationHandler runDegradationHandler;
    @Autowired
    private RuleBuilder ruleBuilder;
    @Autowired
    private RuleByteCodeService ruleByteCodeService;

    /**
     * 升级版本
     *
     * @param model 规则bean名称
     */
    public void upgrade(RuleRegistryModel model) {
        if (!model.isLatest()) {
            return; //历史版本不能修改，不存在版本升级的情况。
        }
        switch (ruleGovernmentConfig.getRunLoaderLevel()) {
            case REDIS:
                // 从redis比对版本升级
                boolean result = versionUpgradeCheckByRedis(model);
                if (result) {
                    break;
                }

            case DB:
                // 从db比对版本升级
                versionUpgradeCheckByDB(model);
                break;

            case MEMORY:
                // 当降级到MEMORY时，从内测中获取规则运行，将不做版本升级比对。
            default:
        }
    }

    /**
     * 通过redis检验规则版本升级
     *
     * @param model
     * @return
     */
    private boolean versionUpgradeCheckByRedis(RuleRegistryModel model) {
        boolean ok = true;
        try {
            String newVersionStr = stringRedisTemplate.opsForValue().get(RuleVersionUtil.getVersionKey(model.getRuleId()));

            // 疑问：当规则更新，redis设置了最新版本，但是过期了规则才运行，此时没主动更新，如何解决？
            // 答：定时任务会在规则过期之前主动更新规则，所以不存在规则更新，没被发现的情况。
            if (StringUtils.isEmpty(newVersionStr)) {
                log.debug("[ruleId:{}]-不存在最新版本", model.getRuleId());
                return ok;
            }

            // 版本一致
            if (Integer.parseInt(newVersionStr) == model.getVersion()) {
                log.debug("[ruleId:{}]-版本一致", model.getRuleId());
                return ok;
            }

            // 规则更新的时候，会将最新的规则同步到redis，且失效时间会比版本（REDIS_RULE_VERSION）的失效时间长。
            String byteCodeStr = stringRedisTemplate.opsForValue().get(RuleVersionUtil.getByteCodeKey(model.getRuleId()));
            RuleByteCode ruleByteCode = JSON.parseObject(byteCodeStr, RuleByteCode.class);
            rulerBeanLoader.registerRule(ruleBuilder.buildNewVersionRule(ruleByteCode));
            log.info("[REDIS]-版本比对-规则版本变更-由V[{}]升级至V[{}]-ruleId:{};", model.getVersion(), newVersionStr, model.getRuleId());

        } catch (PoolException | QueryTimeoutException | RedisException e) {
            runDegradationHandler.incFail();
            ok = false;
            // todo 此处可考虑接入告警，比如：邮件告警、微信告警、短信告警等。
            // todo 告警内容：规则版本从redis缓存比对升级时，redis异常了，异常信息:e.getMessage()
            log.error("[REDIS]-版本比对-Redis异常，ruleId:{}, beanName:{}", model.getRuleId(), model.getRuleBeanName(), e);
        }
        return ok;
    }

    /**
     * 通过DB检验规则版本升级
     *
     * @param model
     * @return
     */
    private void versionUpgradeCheckByDB(RuleRegistryModel model) {
        RuleByteCode ruleByteCode = null;
        try {
            ruleByteCode = ruleByteCodeService.getByRuleId(model.getRuleId());
        } catch (Exception e) {
            runDegradationHandler.incFail();
            // todo 此处可考虑接入告警，比如：邮件告警、微信告警、短信告警等。
            // todo 告警内容：规则版本从DB比对升级时，DB异常了，异常信息:e.getMessage()
            log.info("[DB]-版本比对-DB访问异常-ruleId:{}, beanName:{}", model.getRuleId(), model.getRuleBeanName(), e);
            throw e;
        }
        // 版本一致
        if (ruleByteCode.getVersion() == model.getVersion()) {
            return;
        }
        rulerBeanLoader.registerRule(ruleBuilder.buildNewVersionRule(ruleByteCode));
        log.info("[DB]-版本比对-规则版本发生变更-由V[{}]升级至V[{}]-ruleId:{};", model.getVersion(), ruleByteCode.getVersion(), model.getRuleId());
    }
}
