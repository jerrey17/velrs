package com.ysf.velrs.engine.service;

import com.alibaba.fastjson.JSON;
import com.ysf.velrs.engine.model.ResultInfo;
import com.ysf.velrs.engine.service.exp.*;

import java.util.Map;

/**
 * 单元测试类
 *
 * @Author rui
 * @Date 2021-08-12 18:17
 **/
public class TestRuleWorker implements BaseRuleWorker {

    @Override
    public ResultInfo run(Map<String, String> vars) throws Exception {

        // 第一组
        BooleanExp booleanExp0_0 = new BooleanExp(vars.get("sex"));
        StringExp stringExp0_1 = new StringExp(vars.get("??"));

        boolean resultByExp0 = booleanExp0_0.isTrue() || stringExp0_1.contain("123");

        // 第二组
        StringExp stringExp1_0 = new StringExp(vars.get("??"));

        boolean resultByExp1 = stringExp1_0.startWith("SB");

        // result
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setPass(resultByExp0 && resultByExp1);
        resultInfo.setResultMsg(JSON.toJSONString(vars));
        return resultInfo;
    }


}
