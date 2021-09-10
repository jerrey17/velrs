package com.velrs.engine.mapper;

import com.velrs.engine.domain.RuleHis;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 规则历史表 Mapper接口
 *
 * @date 2021-08-08 16:14:20
 */
@Mapper
public interface RuleHisMapper {

    int insert(@Param("domain") RuleHis domain);

    int insertList(@Param("domains") List<RuleHis> domains);

    int update(@Param("domain") RuleHis domain);

    List<RuleHis> select(@Param("domain") RuleHis domain);

    RuleHis selectOne(@Param("domain") RuleHis domain);

}