package com.velrs.mgt.service.data.impl;

import com.velrs.mgt.domain.Rule;
import com.velrs.mgt.mapper.RuleMapper;
import com.velrs.mgt.service.data.RuleService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 规则表 Service Impl
 *
 * @date 2021-10-08 14:31:19
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

}