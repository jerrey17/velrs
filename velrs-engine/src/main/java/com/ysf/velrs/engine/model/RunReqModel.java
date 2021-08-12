package com.ysf.velrs.engine.model;

import com.ysf.velrs.engine.exception.RuleRunnerBizException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * 规则执行请求参数
 *
 * @Author rui
 * @Date 2021-07-29 10:28
 **/
@Data
public class RunReqModel implements Serializable {

    private String identityId;

    /**
     * 规则id
     */
    private String ruleId;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 规则版本
     * 如果传了，则执行历史规则，不传，默认执行当前最新规则。
     */
    private Integer version;

    /**
     * fact规则参数
     */
    private Map<String, String> fact;

    /**
     * 简单参数验证
     */
    public boolean simpleCheck() {
        if (StringUtils.isEmpty(this.getRuleId())
                || StringUtils.isEmpty(this.getProjectId())
                || this.getFact() == null) {
            throw new RuleRunnerBizException(this.getIdentityId(), "参数异常");
        }
        return true;
    }
}
