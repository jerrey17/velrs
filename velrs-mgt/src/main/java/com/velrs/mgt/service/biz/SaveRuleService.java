package com.velrs.mgt.service.biz;

import com.velrs.mgt.controller.message.CreateReqMessage;
import com.velrs.mgt.domain.Rule;
import com.velrs.mgt.enums.PublishStatusEnum;
import com.velrs.mgt.enums.RuleStatusEnum;
import com.velrs.mgt.enums.TestStatusEnum;
import com.velrs.mgt.exception.BizException;
import com.velrs.mgt.model.CompileModel;
import com.velrs.mgt.service.data.RuleService;
import com.velrs.mgt.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

/**
 * @Author rui
 * @Date 2021-10-09 10:51
 **/
@Slf4j
@Service
public class SaveRuleService {

    @Autowired
    private RuleService ruleService;

    /**
     * 保存或更新
     *
     * @param reqMessage
     */
    @Transactional
    public void saveOrUpdate(CreateReqMessage reqMessage) {
        if (Objects.nonNull(reqMessage.getId())) {
            // edit
            this.update(reqMessage);
        } else {
            // save
            try {
                this.save(reqMessage);
            } catch (DuplicateKeyException e) {
                throw new BizException("规则已存在-ruleId:" + reqMessage.getRuleId());
            }
        }
    }

    /**
     * 更新规则
     *
     * @param reqMessage
     */
    private void update(CreateReqMessage reqMessage) {
        log.info("编辑规则-ruleId:{};id:{}", reqMessage.getId(), reqMessage.getRuleId());
        Rule domain = new Rule();
        domain.setId(reqMessage.getId());
        domain.setRuleId(reqMessage.getRuleId());
        domain.setProjectId(reqMessage.getProjectId());
        Rule one = ruleService.selectOne(domain);
        if (Objects.isNull(one)) {
            throw new BizException("规则不存在");
        }
        if (!Objects.equals(one.getRuleId(), reqMessage.getRuleId()) || !Objects.equals(one.getProjectId(), reqMessage.getProjectId())) {
            throw new BizException("ruleId或projectId错误");
        }
        if (one.getRuleStatus() == RuleStatusEnum.COMPILED.getCode()) {
            throw new BizException("规则已编译，不允许修改");
        }

        // ruleId、projectId 不予许更新。
        Rule rule = new Rule();
        rule.setId(reqMessage.getId());
        rule.setRuleName(reqMessage.getRuleName());
        rule.setLastEditTime(new Date());
        rule.setLastEditUserId(reqMessage.getOperatorUserId());
        rule.setUpdateTime(new Date());
        rule.setDescription(reqMessage.getDescription());
        rule.setRule(JsonUtil.toJsonString(new CompileModel(reqMessage.getRuleId(), reqMessage.getProjectId(), reqMessage.getRule())));

        if (one.getRuleStatus() == RuleStatusEnum.PUBLISHED.getCode()) {
            // 已发布 -> 编辑中
            rule.setRuleStatus(RuleStatusEnum.EDITING.getCode());
            // 已测试 -> 待测试
            rule.setTestStatus(TestStatusEnum.TESTING.getCode());
            // 已发布 -> 待发布
            rule.setPublishStatus(PublishStatusEnum.PUBLISHING.getCode());
        }

        ruleService.update(rule); // 低频，无需考虑并发一致性问题。
        log.info("更新成功-ruleId:{}", reqMessage.getRuleId());

    }

    /**
     * 保存规则
     *
     * @param reqMessage
     */
    private void save(CreateReqMessage reqMessage) {
        log.info("创建规则-ruleId:{}", reqMessage.getId());
        Rule rule = new Rule();
        rule.setRuleId(reqMessage.getRuleId());
        rule.setRuleName(reqMessage.getRuleName());
        rule.setProjectId(reqMessage.getProjectId());
        rule.setRuleStatus(RuleStatusEnum.INIT.getCode());
        rule.setTestStatus(TestStatusEnum.TESTING.getCode());
        rule.setPublishStatus(PublishStatusEnum.PUBLISHING.getCode());
        rule.setLastPublishVersion(0); // default
        rule.setCreateUserId(reqMessage.getOperatorUserId());
        rule.setCreateTime(new Date());
        rule.setUpdateTime(new Date());
        rule.setDescription(reqMessage.getDescription());
        rule.setRule(JsonUtil.toJsonString(new CompileModel(reqMessage.getRuleId(), reqMessage.getProjectId(), reqMessage.getRule())));
        rule.setRuleClassCode("");
        rule.setCallParam("{}");
        ruleService.insert(rule);
        log.info("创建成功-ruleId:{}", reqMessage.getRuleId());

    }
}
