package com.velrs.mgt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
@AllArgsConstructor
public class CompileModel {

    /**
     * 规则id
     */
    private String ruleId;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 规则json
     */
    private List<ConditionModel> rule;

}
