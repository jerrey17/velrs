package com.ysf.velrs.engine.service;

import com.ysf.velrs.engine.domain.RuleByteCode;
import com.ysf.velrs.engine.model.RuleCodeModel;
import com.ysf.velrs.engine.utils.RuleRunnerUtil;
import org.springframework.stereotype.Component;

/**
 * @Author rui
 * @Date 2021-08-08 11:02
 **/
@Component
public class RuleBuilder {

    /**
     * 构造最新版本规则对象
     *
     * @param ruleByteCode
     * @return
     */
    public RuleCodeModel buildNewVersionRule(RuleByteCode ruleByteCode) {
        return RuleCodeModel.builder()
                .projectId(ruleByteCode.getProjectId())
                .ruleId(ruleByteCode.getRuleId())
                .version(ruleByteCode.getVersion())
                .isLatest(true)
                .beanName(RuleRunnerUtil.getRuleBeanName(ruleByteCode.getRuleId(), ruleByteCode.getVersion(), true))
                .ruleClassCode(ruleByteCode.getRuleClassCode())
                .build();
    }
}
