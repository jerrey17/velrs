package com.ysf.velrs.engine;

import com.alibaba.fastjson.JSON;
import com.ysf.velrs.engine.model.ResultInfo;
import com.ysf.velrs.engine.service.BaseRuleWorker;

import java.util.Map;

/**
 * @Author rui
 * @Date 2021-08-12 18:17
 **/
public class TestRuleWorker implements BaseRuleWorker {

    @Override
    public ResultInfo run(Map<String, String> vars) throws Exception {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setPass(true);
        resultInfo.setResultMsg(JSON.toJSONString(vars));
        return resultInfo;
    }
}
