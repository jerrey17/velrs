package com.ysf.velrs.engine.service;

import com.alibaba.fastjson.JSON;
import com.ysf.velrs.engine.model.ResultInfo;
import com.ysf.velrs.engine.service.exp.*;

import java.util.Map;

/**
 * @Author rui
 * @Date 2021-08-12 18:17
 **/
public class TestRuleWorker implements BaseRuleWorker {

    @Override
    public ResultInfo run(Map<String, String> vars) throws Exception {

        BooleanExp booleanExp = new BooleanExp(vars.get("sex"));

        boolean result1 = booleanExp.isTrue();

        boolean result = result1;


        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setPass(result);
        resultInfo.setResultMsg(JSON.toJSONString(vars));
        return resultInfo;
    }
}
