package com.velrs.mgt.service.biz;
import java.util.Date;

import com.velrs.mgt.controller.message.SaveCompileResultReqMessage;
import com.velrs.mgt.domain.Rule;
import com.velrs.mgt.enums.RuleStatusEnum;
import com.velrs.mgt.exception.BizException;
import com.velrs.mgt.service.data.RuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 编译结果处理
 *
 * @Author rui
 * @Date 2021-10-09 15:17
 **/
@Slf4j
@Service
public class CompileResultService {

    @Autowired
    private RuleService ruleService;

    /**
     * 保存编译结果
     *
     * @param reqMessage
     */
    public void saveCompileResult(SaveCompileResultReqMessage reqMessage) {

        log.info("保存编译结果-入参:{}", reqMessage);

        Rule domain = new Rule();
        domain.setRuleId(reqMessage.getRuleId());
        Rule one = ruleService.selectOne(domain);
        if(Objects.isNull(one)) {
            throw new BizException("规则不存在，ruleId:" + reqMessage.getRuleId());
        }
        if(one.getRuleStatus() == RuleStatusEnum.PUBLISHED.getCode()) {
            throw new BizException("已发布的规则不予许编译，ruleId:" + reqMessage.getRuleId());
        }

        Rule rule = new Rule();
        rule.setId(one.getId());
        rule.setRuleStatus(RuleStatusEnum.COMPILED.getCode());
        rule.setCompileTime(reqMessage.getCompileTime());
        rule.setLastCompileTime(new Date());
        rule.setLastCompileUserId(reqMessage.getCompiler());
        rule.setUpdateTime(new Date());
        rule.setRuleClassCode(reqMessage.getRuleByteCode());
        rule.setCallParam(reqMessage.getRequestParam());

        ruleService.update(rule);

        log.info("编译保存成功-ruleId:{}", reqMessage.getRuleId());

    }

}
