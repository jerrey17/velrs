package com.velrs.engine.service.data;

import com.velrs.engine.domain.RuleHis;

import java.util.List;

/**
 * 规则历史表 Service
 *
 * @date 2021-08-08 16:14:20
 */
public interface RuleHisService {

    /**
     * 单条插入
     */
    int insert(RuleHis domain);

    /**
     * 多条插入
     */
    int insertList(List<RuleHis> domains);

    /**
     * 更新
     */
    int update(RuleHis domain);

    /**
     * 查询
     */
    List<RuleHis> select(RuleHis domain);

    /**
     * 单条查询
     */
    RuleHis selectOne(RuleHis domain);

    /**
     * 版本查询规则
     *
     * @return
     */
    RuleHis selectByVersion(String ruleId, int version);


}