package com.velrs.mgt.mapper;

import com.velrs.mgt.domain.Rule;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 规则表 Mapper接口
 *
 * @date 2021-10-08 14:31:19
 */
@Mapper
public interface RuleMapper {

    int insert(@Param("domain") Rule domain);

    int insertList(@Param("domains") List<Rule> domains);

    int update(@Param("domain") Rule domain);

    List<Rule> select(@Param("domain") Rule domain);

    Rule selectOne(@Param("domain") Rule domain);

}