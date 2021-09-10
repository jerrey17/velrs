package com.velrs.engine.service;

import com.velrs.engine.service.exp.StringExp;
import com.velrs.engine.model.ResultInfo;

import java.util.Map;

/**
 * 可执行规则
 *
 * @Author 张三
 * @Date 1, 630, 222, 777, 817
 **/
public class TestRuleWorker implements BaseRuleWorker {


    @Override
    public ResultInfo run(Map<String, String> vars) throws Exception {
        StringExp stringexp0_0 = new StringExp(vars.get("phoneNo"));
        StringExp stringexp0_1 = new StringExp(vars.get("phoneNo"));
        boolean result0 = stringexp0_0.matches(vars.get("myPhone")) && stringexp0_1.endWith("6492");
        StringExp stringexp1_0 = new StringExp(vars.get("phoneNo"));
        StringExp stringexp1_1 = new StringExp(vars.get("phoneNo"));
        boolean result1 = stringexp1_0.contain(vars.get("myPhone")) && stringexp1_1.startWith("158");
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setPass(result0 || result1);
        return resultInfo;
    }
}
