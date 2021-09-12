package com.velrs.engine.service.ruleload;

import com.alibaba.fastjson.JSON;
import com.velrs.engine.config.RuleGovernmentConfig;
import com.velrs.engine.constant.RuleRunnerConstant;
import com.velrs.engine.domain.RuleByteCode;
import com.velrs.engine.model.RuleRegistryModel;
import com.velrs.engine.service.data.RuleByteCodeService;
import com.velrs.engine.service.manage.RunDegradationHandler;
import com.velrs.engine.utils.RuleVersionUtil;
import io.lettuce.core.RedisException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.redis.connection.PoolException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

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
            final String versionKey = RuleVersionUtil.getVersionKey(model.getRuleId());
            String newVersionStr = stringRedisTemplate.opsForValue().get(versionKey);
            if (StringUtils.isEmpty(newVersionStr)) {
                log.debug("[ruleId:{}]-规则版本不存在，重新加载规则到redis", model.getRuleId());
                RuleByteCode ruleByteCode = ruleByteCodeService.getByRuleId(model.getRuleId());
                // 规则字节码对象写入redis（重新加载的就是最新的字节码）
                stringRedisTemplate.opsForValue().set(RuleVersionUtil.getByteCodeKey(model.getRuleId()),
                        JSON.toJSONString(ruleBuilder.buildNewVersionRule(ruleByteCode)),
                        RuleRunnerConstant.VERSION_UP_TIME_OUT, TimeUnit.MINUTES);
                // 升级版本：版本号写入redis
                // 注：
                // 1.版本的过期时间要比规则字节码的过期时间要早，避免runner在版本升级过程中，规则字节码过期，无法正常升级规则。
                // 2.规则字节码写入redis必须在版本号写入redis之前。
                stringRedisTemplate.opsForValue().set(versionKey,
                        String.valueOf(ruleByteCode.getVersion()),
                        RuleRunnerConstant.VERSION_UP_TIME_OUT - 5, TimeUnit.MINUTES);
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
