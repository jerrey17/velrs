package com.velrs.engine.service.data.impl;

import com.velrs.engine.domain.RuleHis;
import com.velrs.engine.service.data.RuleHisService;
import com.velrs.engine.mapper.RuleHisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 规则历史表 Service Impl
 *
 * @date 2021-08-08 16:14:20
 */
@Service
public class RuleHisServiceImpl implements RuleHisService {

    @Autowired
    private RuleHisMapper mapper;

    /**
     * 单条插入
     */
    @Override
    public int insert(RuleHis domain) {
        return mapper.insert(domain);
    }

    /**
     * 多条插入
     */
    @Override
    public int insertList(List<RuleHis> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return 0;
        }
        return mapper.insertList(domains);
    }

    /**
     * 更新
     */
    @Override
    public int update(RuleHis domain) {
        return mapper.update(domain);
    }

    /**
     * 查询
     */
    @Override
    public List<RuleHis> select(RuleHis domain) {
        return Optional.ofNullable(mapper.select(domain)).orElse(Collections.emptyList());
    }

    /**
     * 单条查询
     */
    @Override
    public RuleHis selectOne(RuleHis domain) {
        return mapper.selectOne(domain);
    }

    @Override
    public RuleHis selectByVersion(String ruleId, int version) {
        RuleHis domain = new RuleHis();
        domain.setRuleId(ruleId);
        domain.setVersion(version);
        return mapper.selectOne(domain);
    }

}