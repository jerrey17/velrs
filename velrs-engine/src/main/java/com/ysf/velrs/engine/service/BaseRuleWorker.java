package com.ysf.velrs.engine.service;

import com.ysf.velrs.engine.model.ResultInfo;

import java.util.Map;

/**
 * 规则运行工作接口
 * 所有生成的规则，都实现了该接口来提供能力。
 *
 * @Author rui
 * @Date 2021-08-08 15:40
 **/
public interface BaseRuleWorker {

    /**
     * 规则运行的方法
     *
     * @param vars 规则事实对象列表
     */
    ResultInfo run(Map<String, String> vars) throws Exception;
}
