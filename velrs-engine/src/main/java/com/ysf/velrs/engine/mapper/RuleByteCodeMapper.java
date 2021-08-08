package com.ysf.velrs.engine.mapper;

import com.ysf.velrs.engine.domain.RuleByteCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 规则字节码表 Mapper接口
 *
 * @date 2021-07-29 10:21:00
 */
@Mapper
public interface RuleByteCodeMapper {

    int insert(@Param("domain") RuleByteCode domain);

    int insertList(@Param("domains") List<RuleByteCode> domains);

    int update(@Param("domain") RuleByteCode domain);

    List<RuleByteCode> select(@Param("domain") RuleByteCode domain);

    RuleByteCode selectOne(@Param("domain") RuleByteCode domain);

    /**
     * 验证db是否正常
     *
     * @return
     */
    String selectOK();

    /**
     * ruleId获取RuleByteCode对象
     *
     * @param ruleId
     * @return
     */
    RuleByteCode selectByRuleId(@Param("ruleId") String ruleId);

}