package com.velrs.engine.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author rui
 * @Date 2022-04-30 20:32
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RunMatchRespModel {

    /**
     * match source id
     */
    private String sourceId;
    /**
     * match target id
     */
    private String targetId;
    /**
     * rule run resp
     */
    RunRespModel runResp;

    public static RunMatchRespModel responseFailure(String code, String msg) {
        return RunMatchRespModel.builder()
                .runResp(RunRespModel.responseFailure(code, msg))
                .build();
    }

    /**
     * 配对失败构造响应参数
     *
     * @param sourceId
     * @return
     */
    public static RunMatchRespModel responseMatchFailure(String sourceId) {
        return RunMatchRespModel.builder()
                .sourceId(sourceId)
                .runResp(RunRespModel.responseSuccess("配对失败", false))// 运行成功，但没匹配到结果
                .build();
    }

    /**
     * 根据规则运行结果构造配对响应参数
     *
     * @param sourceId
     * @param targetId
     * @param resultInfo
     * @return
     */
    public static RunMatchRespModel responseByRunResponse(String sourceId, String targetId, ResultInfo resultInfo) {
        RunMatchRespModelBuilder builder = RunMatchRespModel.builder();
        if (resultInfo.isPass()) {
            builder.targetId(targetId);
        }
        return builder
                .runResp(RunRespModel.responseSuccess(resultInfo.getResultMsg(), resultInfo.isPass()))
                .sourceId(sourceId)
                .build();
    }
}
