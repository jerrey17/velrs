package com.velrs.mgt.service.data.impl;

import com.velrs.mgt.domain.RuleByteCode;
import com.velrs.mgt.mapper.RuleByteCodeMapper;
import com.velrs.mgt.service.data.RuleByteCodeService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 规则字节码表 Service Impl
 *
 * @date 2021-10-08 14:31:19
 */
@Service
public class RuleByteCodeServiceImpl implements RuleByteCodeService {

    @Autowired
    private RuleByteCodeMapper mapper;

    /**
     * 单条插入
     */
    @Override
    public int insert(RuleByteCode domain) {
        return mapper.insert(domain);
    }

    /**
     * 多条插入
     */
    @Override
    public int insertList(List<RuleByteCode> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return 0;
        }
        return mapper.insertList(domains);
    }

    /**
     * 更新
     */
    @Override
    public int update(RuleByteCode domain) {
        return mapper.update(domain);
    }

    /**
     * 查询
     */
    @Override
    public List<RuleByteCode> select(RuleByteCode domain) {
        return Optional.ofNullable(mapper.select(domain)).orElse(Collections.emptyList());
    }

    /**
     * 单条查询
     */
    @Override
    public RuleByteCode selectOne(RuleByteCode domain) {
        return mapper.selectOne(domain);
    }

}