package com.velrs.mgt.mapper;

import com.velrs.mgt.domain.RuleHis;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 规则历史表 Mapper接口
 *
 * @date 2021-10-08 14:31:19
 */
@Mapper
public interface RuleHisMapper {

    int insert(@Param("domain") RuleHis domain);

    int insertList(@Param("domains") List<RuleHis> domains);

    int update(@Param("domain") RuleHis domain);

    List<RuleHis> select(@Param("domain") RuleHis domain);

    RuleHis selectOne(@Param("domain") RuleHis domain);

}