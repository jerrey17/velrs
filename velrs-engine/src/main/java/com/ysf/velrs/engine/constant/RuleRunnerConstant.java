package com.ysf.velrs.engine.constant;

/**
 * @Author rui
 * @Date 2021-08-07 21:02
 **/
public interface RuleRunnerConstant {

    /****** rule common ******/
    String UNDER_LINE = "_";
    int LIMIT = 100;
    int ZERO = 0;

    /****** rule regex ******/
    String INT_REGEX = "^-?\\d+$";
    String FLOAT_REGEX = "^-?\\d+([.]\\d{1,4})?$";

    /****** rule constant ******/
    //规则bean或规则类的名称的前缀
    String NAME_PREFIX = "R_";
    String VERSION_TAG = "_V";
    String TEST_TAG = "TEST_";

    //runner context name
    String RUNNER_CONTEXT_BEAN_NAME = "context";

    /****** redis key ******/
    String REDIS_HASH_INIT_VAR = "rule.runner.hash.init.var";
    // 规则版本控制key
    String REDIS_RULE_VERSION = "rule_version_";
    // 规节码
    String REDIS_RULE_BYTE_CODE = "rule_byte_code_";

    //版本升级的redis超时时间
    long VERSION_UP_TIME_OUT = 30;
}
