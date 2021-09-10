package com.velrs.engine.utils;

import com.velrs.engine.constant.RuleRunnerConstant;

/**
 * @Author rui
 * @Date 2021-08-08 10:56
 **/
public class RuleVersionUtil {

    public static String getVersionKey(String ruleId) {
        return RuleRunnerConstant.REDIS_RULE_VERSION + ruleId;
    }

    public static String getByteCodeKey(String ruleId) {
        return RuleRunnerConstant.REDIS_RULE_BYTE_CODE + ruleId;
    }
}
