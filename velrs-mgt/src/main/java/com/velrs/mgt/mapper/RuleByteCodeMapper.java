package com.velrs.mgt.mapper;

import com.velrs.mgt.domain.RuleByteCode;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 规则字节码表 Mapper接口
 *
 * @date 2021-10-08 14:31:19
 */
@Mapper
public interface RuleByteCodeMapper {

    int insert(@Param("domain") RuleByteCode domain);

    int insertList(@Param("domains") List<RuleByteCode> domains);

    int update(@Param("domain") RuleByteCode domain);

    List<RuleByteCode> select(@Param("domain") RuleByteCode domain);

    RuleByteCode selectOne(@Param("domain") RuleByteCode domain);

}