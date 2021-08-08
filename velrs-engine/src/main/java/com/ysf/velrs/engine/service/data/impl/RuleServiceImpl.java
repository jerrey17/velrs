package com.ysf.velrs.engine.service.data.impl;

import com.ysf.velrs.engine.domain.Rule;
import com.ysf.velrs.engine.mapper.RuleMapper;
import com.ysf.velrs.engine.service.data.RuleService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 规则表 Service Impl
 *
 * @date 2021-08-08 16:30:45
 */
@Service
public class RuleServiceImpl implements RuleService {

    @Autowired
    private RuleMapper mapper;

    /**
     * 单条插入
     */
    @Override
    public int insert(Rule domain) {
        return mapper.insert(domain);
    }

    /**
     * 多条插入
     */
    @Override
    public int insertList(List<Rule> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return 0;
        }
        return mapper.insertList(domains);
    }

    /**
     * 更新
     */
    @Override
    public int update(Rule domain) {
        return mapper.update(domain);
    }

    /**
     * 查询
     */
    @Override
    public List<Rule> select(Rule domain) {
        return Optional.ofNullable(mapper.select(domain)).orElse(Collections.emptyList());
    }

    /**
     * 单条查询
     */
    @Override
    public Rule selectOne(Rule domain) {
        return mapper.selectOne(domain);
    }

    @Override
    public Rule selectByRuleId(String ruleId) {
        Rule domain = new Rule();
        domain.setRuleId(ruleId);
        return mapper.selectOne(domain);
    }

}