package com.velrs.mgt.service.data;

import com.velrs.mgt.domain.RuleHis;
import java.util.List;

/**
 * 规则历史表 Service
 *
 * @date 2021-10-08 14:31:19
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

}