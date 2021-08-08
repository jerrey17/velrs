package com.ysf.velrs.engine.service.ruleload;

import com.ysf.velrs.engine.domain.Rule;
import com.ysf.velrs.engine.domain.RuleByteCode;
import com.ysf.velrs.engine.domain.RuleHis;
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

    /**
     * 构造历史版本规则对象
     *
     * @param ruleHis
     * @param projectId
     * @return
     */
    public RuleCodeModel buildHisVersionRule(RuleHis ruleHis, String projectId) {
        return RuleCodeModel.builder()
                .projectId(projectId)
                .ruleId(ruleHis.getRuleId())
                .version(ruleHis.getVersion())
                .isLatest(false)
                .beanName(RuleRunnerUtil.getRuleBeanName(ruleHis.getRuleId(), ruleHis.getVersion(), false))
                .ruleClassCode(ruleHis.getRuleClassCode())
                .build();
    }

    /**
     * 构造测试版本规则对象
     *
     * @param rule
     * @return
     */
    public RuleCodeModel buildTestVersionRule(Rule rule, String beanName) {
        return RuleCodeModel.builder()
                .projectId(rule.getProjectId())
                .ruleId(rule.getRuleId())
                .version(-99)
                .isLatest(false)
                .beanName(beanName)
                .ruleClassCode(rule.getRuleClassCode())
                .build();
    }
}
