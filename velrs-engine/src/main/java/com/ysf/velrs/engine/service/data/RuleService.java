package com.ysf.velrs.engine.service.data;

import com.ysf.velrs.engine.domain.Rule;

import java.util.List;

/**
 * 规则表 Service
 *
 * @date 2021-08-08 16:30:45
 */
public interface RuleService {

    /**
     * 单条插入
     */
    int insert(Rule domain);

    /**
     * 多条插入
     */
    int insertList(List<Rule> domains);

    /**
     * 更新
     */
    int update(Rule domain);

    /**
     * 查询
     */
    List<Rule> select(Rule domain);

    /**
     * 单条查询
     */
    Rule selectOne(Rule domain);

    /**
     * 查询
     *
     * @param ruleId
     * @return
     */
    Rule selectByRuleId(String ruleId);

}