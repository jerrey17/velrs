package com.ysf.velrs.engine.service.ruleload;

import com.ysf.velrs.engine.model.RuleCodeModel;
import com.ysf.velrs.engine.model.RuleRegistryModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 规则注册表，他管理着整个规则的生命周期
 *
 * @Author rui
 * @Date 2021-08-07 21:06
 **/
@Slf4j
@Component
public class RuleRegistry {

    // 规则注册表，管理规则的生命周期
    private static final Map<String, RuleRegistryModel> RULE_REGISTRY_MAP = new ConcurrentHashMap<>(256);
    // 空的bean
    public static final String EMPTY_NAME = "EMPTY";
    // 空版本号
    private static final int EMPTY_VERSION = -1;

    /**
     * 获取规则注册表列表
     *
     * @return
     */
    public Map<String, RuleRegistryModel> getRuleRegistries() {
        return RULE_REGISTRY_MAP;
    }

    /**
     * 是否已注册
     * 没注册的定义：bean不存在 或 注册的是一个空对象
     */
    public boolean isRegistered(final String beanName) {
        if (StringUtils.isEmpty(beanName)) {
            return false;
        }
        RuleRegistryModel model = RULE_REGISTRY_MAP.get(beanName);
        // 没注册
        if (model == null) {
            return false;
        }
        // 注册的是一个空的bean
        if (StringUtils.equals(EMPTY_NAME, model.getRuleBeanName())) {
            return false;
        }
        return true;
    }

    /**
     * 是否注册了一个空对象
     * 注册空对象是为了防止接口穿透问题，避免不存在的恶意参数强刷接口，导致底层缓存或db崩溃。
     *
     * @param beanName
     * @return
     */
    public boolean isEmptyRegistered(final String beanName) {
        RuleRegistryModel model = RULE_REGISTRY_MAP.get(beanName);
        if (model == null) {
            return false;
        }
        if (StringUtils.equals(EMPTY_NAME, model.getRuleBeanName())) {
            model.getCallTimes().incrementAndGet();
            // 允许不存在的规则访问n次，防止先调用，后注册的情况发生。
            return model.getCallTimes().intValue() > 2;
        }
        return false;
    }

    /**
     * 获取注册规则
     *
     * @param beanName
     * @return
     */
    public RuleRegistryModel getRuleRegistry(final String beanName) {
        return RULE_REGISTRY_MAP.get(beanName);
    }

    /**
     * 注册规则
     *
     * @param beanName 规则bean名称
     */
    public void registerRule(final String beanName, final RuleCodeModel model) {
        RULE_REGISTRY_MAP.put(beanName, new RuleRegistryModel(model.getRuleId(), model.getVersion(), model.isLatest(), beanName));
    }

    /**
     * 注册一个空的规则bean
     * 注册空规则可以防止穿透。
     *
     * @param beanName
     */
    public void registerEmptyRule(final String beanName) {
        if (RULE_REGISTRY_MAP.get(beanName) == null) {
            synchronized (beanName) {
                if (RULE_REGISTRY_MAP.get(beanName) == null) {
                    RULE_REGISTRY_MAP.put(beanName, new RuleRegistryModel(EMPTY_NAME, EMPTY_VERSION, EMPTY_NAME));
                }
            }
        }
    }

    /**
     * 销毁对象
     *
     * @param beanName
     */
    public void destroy(final String beanName) {
        RULE_REGISTRY_MAP.remove(beanName);
    }

    /**
     * 注册的规则被调用了一次
     *
     * @param beanName
     */
    public void calInc(final String beanName) {
        RULE_REGISTRY_MAP.get(beanName).getCallTimes().incrementAndGet();
    }
}
