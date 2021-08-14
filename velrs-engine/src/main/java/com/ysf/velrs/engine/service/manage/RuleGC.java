package com.ysf.velrs.engine.service.manage;

import com.ysf.velrs.engine.config.RuleGovernmentConfig;
import com.ysf.velrs.engine.service.ruleload.RuleRegistry;
import com.ysf.velrs.engine.service.ruleload.RulerBeanLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author rui
 * @Date 2021-08-14 14:40
 **/
@Slf4j
@Component
public class RuleGC {

    @Autowired
    private RuleGovernmentConfig ruleGovernmentConfig;
    @Autowired
    private RuleRegistry ruleRegistry;
    @Autowired
    private RulerBeanLoader rulerBeanLoader;

    /**
     * 规则回收。
     * 根据标记法得到的权重判断规则的生命体征。当权重达到一定数值的时候会对规则进行销毁。
     */
    public RuleGC gc() {
        if (ruleRegistry.getRuleRegistries().size() == 0) {
            return this;
        }
        ruleRegistry.getRuleRegistries().forEach((k, v) -> {
            try {
                /**
                 * TODO 这里简单实现，后面可以优化成：采用规则来管理规则
                 * case1：是否是空对象
                 * case2：权重
                 * case3：白名单 todo 后续可考虑高频规则，配置进白名单，不进行回收。
                 */
                if (StringUtils.equals(RuleRegistry.EMPTY_NAME, v.getRuleBeanName())) {
                    log.warn("规则实例[]空对象-销毁...", k);
                    rulerBeanLoader.destroyRule(k);
                }
                if (v.getWeight().intValue() > 100) {
                    log.warn("规则实例[]权重达到[{}]-销毁...", k, v.getWeight().intValue());
                    rulerBeanLoader.destroyRule(k);
                }
            } catch (Exception e) {
                log.error("规则回收异常！beanName:{}", k, e);
            }
        });
        return this;
    }

    /**
     * 标记法。
     * 权重越高，使用率越低。
     * 根据规则的访问频率、次数、对象性质来判断是否要对规则进行标记，标记的权重值根据规则返回的结果值进行累加。
     */
    public RuleGC mark() {
        if (ruleRegistry.getRuleRegistries().size() == 0) {
            return this;
        }
        ruleRegistry.getRuleRegistries().forEach((k, v) -> {
            try {
                /**
                 * 每分钟运行次数
                 * 是否是空对象
                 * 访问总次数 // 较大者可考虑延迟回收
                 * 白名单 todo 后续可考虑高频规则，配置进白名单，不进行标记。
                 */
                int weight = 0;
                double callTimesPerMin = (v.getCallTimes().intValue() - v.getLastCallTimes()) / ruleGovernmentConfig.getGcStep();
                if (callTimesPerMin < 1) {
                    weight = weight + 10;   // 权重+10

                } else if (callTimesPerMin >= 1 && callTimesPerMin < 100) {
                    weight = weight + 1;    // 权重+1

                } else {
                    weight = weight + 0;    // 权重不变
                }

                if (StringUtils.equals(RuleRegistry.EMPTY_NAME, v.getRuleBeanName())) {
                    weight = weight + 100;  // 空对象，权重+100
                }

                int total = v.getWeight().addAndGet(weight);
                log.warn("规则实例[{}]权重累加: {}，累加后权重值为：{}", v.getRuleBeanName(), weight, total);
                v.setLastCallTimes(v.getCallTimes().intValue());
            } catch (Exception e) {
                log.error("规则标记异常！beanName:{}", k, e);
            }
        });
        return this;
    }
}
