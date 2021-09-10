package com.velrs.engine.controller.message;

import com.velrs.engine.model.ConditionModel;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @Author rui
 * @Date 2021-08-18 16:20
 **/
@Data
public class CompileReqMessage {

    /**
     * 规则id
     */
    @Size(max = 64, message = "ruleId不超过{max}位")
    @NotBlank(message = "ruleId不能为空")
    private String ruleId;

    /**
     * 项目id
     */
    @Size(max = 64, message = "projectId不超过{max}位")
    @NotBlank(message = "projectId不能为空")
    private String projectId;

    /**
     * 规则json
     */
    @NotNull(message = "rule不能为空")
    @Valid
    private List<ConditionModel> rule;

    /**
     * 操作人
     */
    @Size(max = 64, message = "operator不超过{max}位")
    @NotBlank(message = "operator不能为空")
    private String operator;


}
