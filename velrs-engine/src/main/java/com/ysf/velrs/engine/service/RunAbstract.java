package com.ysf.velrs.engine.service;

import com.ysf.velrs.engine.domain.RuleByteCode;
import com.ysf.velrs.engine.domain.RuleHis;
import com.ysf.velrs.engine.exception.RuleRunnerBizException;
import com.ysf.velrs.engine.model.RuleCodeModel;
import com.ysf.velrs.engine.model.RunReqModel;
import com.ysf.velrs.engine.service.data.RuleByteCodeService;
import com.ysf.velrs.engine.service.data.RuleHisService;
import com.ysf.velrs.engine.service.ruleload.RuleBuilder;
import com.ysf.velrs.engine.service.ruleload.RuleRegistry;
import com.ysf.velrs.engine.service.ruleload.RuleVersionUpgrade;
import com.ysf.velrs.engine.service.ruleload.RulerBeanLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 规则运行抽象类
 *
 * @Author rui
 * @Date 2021-08-08 15:57
 **/
@Slf4j
public abstract class RunAbstract {

    @Autowired
    private RuleRegistry ruleRegistry;
    @Autowired
    private RulerBeanLoader rulerBeanLoader;
    @Autowired
    private RuleVersionUpgrade ruleVersionUpgrade;
    @Autowired
    private RuleBuilder ruleBuilder;
    @Autowired
    private RuleByteCodeService ruleByteCodeService;
    @Autowired
    private RuleHisService ruleHisService;


    /**
     * 运行检查
     *
     * @param model
     * @param beanName
     */
    protected void runCheck(RunReqModel model, String beanName) {

        if (ruleRegistry.isRegistered(beanName)) {
            //规则已注册，检查版本升级
            ruleVersionUpgrade.upgrade(ruleRegistry.getRuleRegistry(beanName));
            return;
        }
        if (ruleRegistry.isEmptyRegistered(beanName)) {
            log.debug("规则验证-注册的空对象-beanName:{}; ruleId:{}", beanName, model.getRuleId());
            throw new RuleRunnerBizException(model.getIdentityId(),
                    String.format("项目[%s]所属的规则[%s]没注册", model.getProjectId(), model.getRuleId()));
        }

        RuleByteCode ruleByteCode = ruleByteCodeService.getByRuleId(model.getRuleId());
        // 规则不存在
        if (ruleByteCode == null) {
            ruleRegistry.registerEmptyRule(beanName);
            throw new RuleRunnerBizException(model.getIdentityId(),
                    String.format("项目[%s]所属的规则[%s]没注册", model.getProjectId(), model.getRuleId()));
        }

        RuleCodeModel codeModel = null;
        // 约定：入参带版本号，运行历史版本；入参不带版本号，运行最新版本。
        if (model.getVersion() != null && model.getVersion() > 0) {
            // 获取历史版本
            RuleHis xmlVersion = ruleHisService.selectByVersion(model.getRuleId(), model.getVersion());
            if (xmlVersion == null) {
                throw new RuleRunnerBizException(model.getIdentityId(),
                        String.format("项目[%s]所属的规则[%s]版本[%s]不存在", model.getProjectId(), model.getRuleId(), model.getVersion()));
            }
            codeModel = ruleBuilder.buildHisVersionRule(xmlVersion, model.getProjectId());
        } else {
            codeModel = ruleBuilder.buildNewVersionRule(ruleByteCode);
        }

        synchronized (model.getRuleId()) {
            if (ruleRegistry.isRegistered(beanName)) {
                return;
            }
            rulerBeanLoader.registerRule(codeModel);
        }
    }
}
