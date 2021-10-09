package com.velrs.mgt.service.data;

import com.velrs.mgt.domain.RuleByteCode;
import java.util.List;

/**
 * 规则字节码表 Service
 *
 * @date 2021-10-08 14:31:19
 */
public interface RuleByteCodeService {

    /**
     * 单条插入
     */
    int insert(RuleByteCode domain);

    /**
     * 多条插入
     */
    int insertList(List<RuleByteCode> domains);

    /**
     * 更新
     */
    int update(RuleByteCode domain);

    /**
     * 查询
     */
    List<RuleByteCode> select(RuleByteCode domain);

    /**
     * 单条查询
     */
    RuleByteCode selectOne(RuleByteCode domain);

}