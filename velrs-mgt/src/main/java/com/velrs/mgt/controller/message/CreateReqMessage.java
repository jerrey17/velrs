package com.velrs.mgt.controller.message;

import com.velrs.mgt.model.ConditionModel;
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
public class CreateReqMessage {

    /**
     * 自增id
     * 如果传了，则表示更新。
     * 如果没传，则表示新增。
     */
    private Integer id;

    /**
     * 规则id, 全局唯一。
     */
    @Size(max = 64, message = "ruleId不超过{max}位")
    @NotBlank(message = "ruleId不能为空")
    private String ruleId;

    /**
     * 规则名称
     */
    @Size(max = 128, message = "ruleName不超过{max}位")
    @NotBlank(message = "ruleName不能为空")
    private String ruleName;

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
     * 操作人用户ID
     */
    @NotNull
    private Long operatorUserId;

    /**
     * 规则描述
     */
    @Size(max = 256, message = "description不超过{max}位")
    private String description;


}
