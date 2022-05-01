package com.velrs.engine.model;

import com.velrs.engine.exception.RuleRunnerBizException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 规则执行请求参数
 *
 * @Author rui
 * @Date 2022-04-30 21:28
 **/
@Data
public class RunMatchReqModel extends RunReqModel {

    /**
     * key：id
     */
    public static final String SOURCE_ID = "sourceId";
    public static final String TARGET_ID = "targetId";

    /**
     * 源规则参数
     * 必须put一个key：sourceId
     */
    private List<Map<String, String>> sources;

    /**
     * 目标规则参数
     * 必须put一个key：targetId
     */
    private List<Map<String, String>> targets;

    /**
     * 简单参数验证
     */
    public boolean simpleCheck() {
        if (StringUtils.isEmpty(this.getRuleId())
                || StringUtils.isEmpty(this.getProjectId())
                || this.getSources() == null
                || this.getTargets() == null) {
            throw new RuleRunnerBizException(this.getIdentityId(), "参数异常");
        }
        int sourceSize = this.getSources().size();
        int targetSize = this.getTargets().size();
        if(sourceSize == 0 || targetSize == 0) {
            throw new RuleRunnerBizException(this.getIdentityId(), "参数异常:sources|targets不能为空");
        }
        if(sourceSize > 2000 || targetSize > 2000) {
            throw new RuleRunnerBizException(this.getIdentityId(), "参数异常:sources|targets不能超过2000");
        }
        sources.forEach(data -> {
            if(StringUtils.isBlank(data.get(SOURCE_ID))) {
                throw new RuleRunnerBizException(this.getIdentityId(), "参数异常:match配对，sourceId不能为空");
            }
        });
        targets.forEach(data -> {
            if(StringUtils.isBlank(data.get(TARGET_ID))) {
                throw new RuleRunnerBizException(this.getIdentityId(), "参数异常:match配对，targetId不能为空");
            }
        });
        if(sources.stream().map(map -> map.get(SOURCE_ID)).distinct().count() < sourceSize) {
            throw new RuleRunnerBizException(this.getIdentityId(), "参数异常:match配对，sourceId不能重复");
        }
        if(targets.stream().map(map -> map.get(TARGET_ID)).distinct().count() < targetSize) {
            throw new RuleRunnerBizException(this.getIdentityId(), "参数异常:match配对，targetId不能重复");
        }
        return true;
    }
}
