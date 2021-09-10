package com.velrs.engine.controller.message;

import lombok.Data;

/**
 * @Author rui
 * @Date 2021-08-18 16:20
 **/
@Data
public class CompileRespMessage {

    /**
     * 规则id
     */
    private String ruleId;

    /**
     * 规则字节码
     */
    private String ruleByteCode;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 编译时间
     */
    private long compileTime;

}
