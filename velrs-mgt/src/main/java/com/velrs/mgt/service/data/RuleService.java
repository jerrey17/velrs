package com.velrs.mgt.service.data;

import com.velrs.mgt.domain.Rule;
import java.util.List;

/**
 * 规则表 Service
 *
 * @date 2021-10-08 14:31:19
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

}