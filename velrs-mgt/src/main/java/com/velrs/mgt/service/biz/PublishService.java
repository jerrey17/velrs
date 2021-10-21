package com.velrs.mgt.service.biz;

import com.velrs.mgt.domain.Rule;
import com.velrs.mgt.domain.RuleByteCode;
import com.velrs.mgt.domain.RuleHis;
import com.velrs.mgt.enums.PublishStatusEnum;
import com.velrs.mgt.enums.RuleStatusEnum;
import com.velrs.mgt.enums.TestStatusEnum;
import com.velrs.mgt.exception.BizException;
import com.velrs.mgt.service.data.RuleByteCodeService;
import com.velrs.mgt.service.data.RuleHisService;
import com.velrs.mgt.service.data.RuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

/**
 * 发布业务
 *
 * @Author rui
 * @Date 2021-10-21 18:04
 **/
@Slf4j
@Service
public class PublishService {

    @Autowired
    private RuleService ruleService;
    @Autowired
    private RuleByteCodeService ruleByteCodeService;
    @Autowired
    private RuleHisService ruleHisService;

    /**
     * 发布
     *
     * @param ruleId
     */
    @Transactional
    public void publish(String ruleId) {
        log.info(">> 发布-ruleId:{}", ruleId);

        Rule domain = new Rule();
        domain.setRuleId(ruleId);
        Rule one = ruleService.selectOne(domain);
        if (Objects.isNull(one)) {
            throw new BizException("规则不存在，ruleId:" + ruleId);
        }
        if (one.getPublishStatus() != PublishStatusEnum.PUBLISHING.getCode()) {
            throw new BizException("无需重复发布，ruleId:" + ruleId);
        }
        if (one.getRuleStatus() != RuleStatusEnum.COMPILED.getCode()) {
            throw new BizException("只允许已编译规则发布，ruleId:" + ruleId);
        }
        if (one.getTestStatus() != TestStatusEnum.TESTED.getCode()) {
            throw new BizException("只允许已测试规则发布，ruleId:" + ruleId);
        }

        RuleByteCode ruleByteCode = this.saveRuleByteCode(one);
        this.saveHis(one, ruleByteCode);
        Rule rule = new Rule();
        rule.setId(one.getId());
        rule.setUpdateTime(new Date());
        rule.setRuleStatus(RuleStatusEnum.PUBLISHED.getCode());
        rule.setPublishStatus(PublishStatusEnum.PUBLISHED.getCode());
        ruleService.update(rule); // 低频，无需考虑并发问题
        log.info("规则发布成功-ruleId:{}", ruleId);
    }

    private void saveHis(Rule one, RuleByteCode ruleByteCode) {
        RuleHis his = new RuleHis();
        his.setRuleId(one.getRuleId());
        his.setVersion(Objects.isNull(ruleByteCode) ? 1 : ruleByteCode.getVersion());
        his.setRule(one.getRule());
        his.setRuleClassCode(one.getRuleClassCode());
        his.setCallParam(one.getCallParam());
        his.setCompileTime(one.getCompileTime());
        his.setCreateUserId(one.getCreateUserId());
        his.setCreateTime(new Date());
        his.setUpdateTime(new Date());
        ruleHisService.insert(his);
        log.info("保存历史记录成功-ruleId:{}", one.getRuleId());
    }

    private RuleByteCode saveRuleByteCode(Rule one) {
        RuleByteCode ruleByteCode = ruleByteCodeService.selectByRuleId(one.getRuleId());
        if (Objects.isNull(ruleByteCode)) {
            RuleByteCode saveRuleByteCode = new RuleByteCode();
            saveRuleByteCode.setRuleId(one.getRuleId());
            saveRuleByteCode.setProjectId(one.getProjectId());
            saveRuleByteCode.setVersion(1);
            saveRuleByteCode.setRuleClassCode(one.getRuleClassCode());
            saveRuleByteCode.setRuleSize(one.getRuleClassCode().length());
            saveRuleByteCode.setCallParam(one.getCallParam());
            saveRuleByteCode.setCompileTime(one.getCompileTime());
            saveRuleByteCode.setLastPublishUserId(one.getLastCompileUserId());
            saveRuleByteCode.setCreateTime(new Date());
            saveRuleByteCode.setUpdateTime(new Date());
            ruleByteCodeService.insert(saveRuleByteCode);
            log.info("字节码保存成功-ruleId:{}", one.getRuleId());
            ruleByteCode = saveRuleByteCode;
        } else {
            RuleByteCode updateRuleByteCode = new RuleByteCode();
            updateRuleByteCode.setId(ruleByteCode.getId());
            updateRuleByteCode.setCallParam(one.getCallParam());
            updateRuleByteCode.setVersion(ruleByteCode.getVersion() + 1);// 升级
            updateRuleByteCode.setRuleClassCode(one.getRuleClassCode());
            updateRuleByteCode.setRuleSize(one.getRuleClassCode().length());
            updateRuleByteCode.setCompileTime(one.getCompileTime());
            updateRuleByteCode.setLastPublishUserId(one.getLastCompileUserId());
            updateRuleByteCode.setUpdateTime(new Date());

            int count = ruleByteCodeService.update(updateRuleByteCode);
            if (count != 1) {
                throw new BizException("更新字节码失败了");
            }
            log.info("字节码更新成功-ruleId:{}；version:{}", one.getRuleId(), updateRuleByteCode.getVersion());
            ruleByteCode = updateRuleByteCode;
        }
        return ruleByteCode;
    }


}
