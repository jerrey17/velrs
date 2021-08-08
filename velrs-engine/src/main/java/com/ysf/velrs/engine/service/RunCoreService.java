package com.ysf.velrs.engine.service;

import com.ysf.velrs.engine.exception.RuleExpiredException;
import com.ysf.velrs.engine.exception.RuleRunnerBizException;
import com.ysf.velrs.engine.model.ResultInfo;
import com.ysf.velrs.engine.model.RunReqModel;
import com.ysf.velrs.engine.service.ruleload.RuleRegistry;
import com.ysf.velrs.engine.utils.RuleRunnerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * 规则运行核心实现类
 *
 * @Author rui
 * @Date 2021-08-08 16:23
 **/
@Slf4j
@Service
public class RunCoreService extends RunAbstract implements RunInterface {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private RuleRegistry ruleRegistry;

    /**
     * 运行规则。通过fact参数进行规则匹配，返回匹配成功的结果。
     *
     * @param model 规则运行参数
     * @return 匹配结果
     */
    @Override
    public ResultInfo run(RunReqModel model) {
        String beanName = RuleRunnerUtil.getRuleBeanName(model);
        runCheck(model, beanName);
        try {
            BaseRuleWorker worker = applicationContext.getBean(beanName, BaseRuleWorker.class);
            ResultInfo resultInfo = worker.run(model.getFact());
            ruleRegistry.calInc(beanName);
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
