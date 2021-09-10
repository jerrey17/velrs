package com.velrs.engine.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 规则运行响应参数
 *
 * @Author rui
 * @Date 2021-07-29 10:30
 **/
@Data
public class RunRespModel<T> implements Serializable {

    // 请求响应状态：0：失败（默认）；1：成功
    private int status = 0;

    // 规则结果：false:不通过（默认）；true：通过
    private boolean pass = false;

    // 规则响应数据
    private T data;

    // 编码
    private String code;

    // 信息
    private String msg;

    /**
     * 响应成功
     *
     * @param data 响应数据
     * @param <T>  响应数据类型
     * @return ResponseModel
     */
    public static <T> RunRespModel responseSuccess(T data) {
        RunRespModel runRespModel = new RunRespModel();
        runRespModel.setStatus(1);
        runRespModel.setData(data);
        return runRespModel;
    }

    /**
     * 响应成功，
     *
     * @param data 响应数据
     * @param pass 是否通过
     * @param <T>  响应数据类型
     * @return ResponseModel
     */
    public static <T> RunRespModel responseSuccess(T data, boolean pass) {
        RunRespModel runRespModel = responseSuccess(data);
        runRespModel.setPass(pass);
        return runRespModel;
    }

    /**
     * 响应失败
     *
     * @param code 错误编码
     * @param msg  错误信息
     * @return ResponseModel
     */
    public static RunRespModel responseFailure(String code, String msg) {
        RunRespModel runRespModel = new RunRespModel();
        runRespModel.setData(null);
        runRespModel.setCode(code);
        runRespModel.setMsg(msg);
        return runRespModel;
    }
}
