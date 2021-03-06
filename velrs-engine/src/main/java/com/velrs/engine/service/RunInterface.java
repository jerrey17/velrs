package com.velrs.engine.service;

import com.velrs.engine.model.ResultInfo;
import com.velrs.engine.model.RunReqModel;

/**
 * @Author rui
 * @Date 2021-08-08 15:54
 **/
public interface RunInterface {

    /**
     * 规则运行入口
     *
     * @param model
     * @return
     */
    ResultInfo run(RunReqModel model);
}
