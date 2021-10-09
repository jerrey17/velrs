package com.velrs.mgt.controller.message;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author rui
 * @Date 2021-10-09 15:35
 **/
@Data
public class SaveCompileResultReqMessage {

    /**
     * 规则id
     */
    @Size(max = 32, message = "ruleId不超过{max}位")
    @NotBlank(message = "ruleId不能为空")
    private String ruleId;

    /**
     * 规则字节码
     */
    @NotBlank(message = "ruleByteCode不能为空")
    private String ruleByteCode;

    /**
     * 请求参数
     */
    @Size(max = 1024, message = "requestParam不超过{max}位")
    @NotBlank(message = "requestParam不能为空")
    private String requestParam;

    /**
     * 编译时间
     */
    @NotNull(message = "compileTime不能为空")
    private Long compileTime;

    /**
     * 编译人
     */
    public Long compiler;
}
