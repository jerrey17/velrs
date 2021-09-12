package com.velrs.engine.constant;

/**
 * @Author rui
 * @Date 2021-08-07 21:02
 **/
public interface RuleRunnerConstant {

    int ZERO = 0;
    String UNDER_LINE = "_";

    String NAME_PREFIX = "R_";
    String TEST_TAG = "T_";
    String VERSION_TAG = "_V";
    // 规则模板
    String RULE_TEMPLATE = "rule_template.ftl";

    // 规则版本控制redis key
    String REDIS_RULE_VERSION = "rule_version_";
    // 规节码redis key
    String REDIS_RULE_BYTE_CODE = "rule_byte_code_";

    //版本升级的redis超时时间
    long VERSION_UP_TIME_OUT = 30;

}
