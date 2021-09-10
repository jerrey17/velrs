package com.velrs.engine.service;

import com.velrs.engine.domain.Rule;
import com.velrs.engine.exception.RuleExpiredException;
import com.velrs.engine.exception.RuleRunnerBizException;
import com.velrs.engine.service.data.RuleService;
import com.velrs.engine.service.ruleload.RuleBuilder;
import com.velrs.engine.service.ruleload.RulerBeanLoader;
import com.velrs.engine.utils.RuleRunnerUtil;
import com.velrs.engine.model.ResultInfo;
import com.velrs.engine.model.RunReqModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 规则运行测试实现类
 *
 * @Author rui
 * @Date 2021-08-08 16:27
 **/
@Slf4j
@Service
public class RunTestService extends RunAbstract implements RunInterface {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private RuleService ruleService;
    @Autowired
    private RulerBeanLoader rulerBeanLoader;
    @Autowired
    private RuleBuilder ruleBuilder;

    /**
     * 测试规则执行
     *
     * @param model
     * @return
     */
    @Override
    public ResultInfo run(RunReqModel model) {
        Rule rule = ruleService.selectByRuleId(model.getRuleId());
        if (Objects.isNull(rule)) {
            throw new RuleRunnerBizException(model.getIdentityId(), "规则不存在");
        }
        if (!rule.isCompile()) {
            throw new RuleRunnerBizException(model.getIdentityId(), "规则未编译");
        }
        String beanName = RuleRunnerUtil.getTestRuleBeanName(rule.getRuleId());
        try {
            // 注册测试规则
            rulerBeanLoader.registerRule(ruleBuilder.buildTestVersionRule(rule, beanName));
            // 获取bean
            BaseRuleWorker worker = applicationContext.getBean(beanName, BaseRuleWorker.class);
            // run test
            ResultInfo resultInfo = worker.run(model.getFact());
            return resultInfo;
        } catch (NoSuchBeanDefinitionException e) {
            throw new RuleRunnerBizException(model.getIdentityId(),
                    String.format("项目[%s]所属的规则[%s]不存在或没发布", model.getProjectId(), model.getRuleId()));
        } catch (RuleExpiredException e) {
            e.setIdentityId(model.getIdentityId());
            throw e;
        } catch (Exception e) {
            throw new RuleRunnerBizException(model.getIdentityId(), "规则运行时异常", e);
        }
    }
}
