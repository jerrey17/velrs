package com.velrs.mgt.service.biz;

import com.velrs.mgt.domain.Rule;
import com.velrs.mgt.enums.RuleStatusEnum;
import com.velrs.mgt.enums.TestStatusEnum;
import com.velrs.mgt.exception.BizException;
import com.velrs.mgt.service.data.RuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * 测试结果处理
 *
 * @Author rui
 * @Date 2021-10-21 17:50
 **/
@Slf4j
@Service
public class TestResultService {

    @Autowired
    private RuleService ruleService;

    /**
     * 扭转测试结果
     * @param ruleId
     */
    public void saveTestPass(String ruleId) {

        log.info("扭转测试结果-入参:{}", ruleId);

        Rule domain = new Rule();
        domain.setRuleId(ruleId);
        Rule one = ruleService.selectOne(domain);
        if(Objects.isNull(one)) {
            throw new BizException("规则不存在，ruleId:" + ruleId);
        }
        if(one.getRuleStatus() != RuleStatusEnum.COMPILED.getCode()) {
            throw new BizException("只允许已编译规则更新测试结果，ruleId:" + ruleId);
        }
        if(one.getTestStatus() != TestStatusEnum.TESTING.getCode()) {
            throw new BizException("已测试规则不能反复变更测试结果，ruleId:" + ruleId);
        }

        Rule rule = new Rule();
        rule.setId(one.getId());
        rule.setUpdateTime(new Date());
        rule.setTestStatus(TestStatusEnum.TESTED.getCode());
        ruleService.update(rule);

        log.info("更新测试结果成功-ruleId:{}", ruleId);
    }


}
