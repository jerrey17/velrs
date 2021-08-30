package com.ysf.velrs.engine.service.compile;

import com.ysf.velrs.engine.exception.CompileException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author rui
 * @Date 2021-08-19 15:00
 **/
public class CompileCondition implements CompileInterface {

    private List<CompileInterface> exps;
    private String logic;
    private int conditionIndex;

    public CompileCondition(List<CompileInterface> exps, String logic, int conditionIndex) {
        this.exps = exps;
        this.logic = logic;
        this.conditionIndex = conditionIndex;
    }

    @Override
    public String getExpObj() {
        return exps.stream().map(CompileInterface::getExpObj).collect(Collectors.joining());
    }

    @Override
    public String getLogic() {
        if (conditionIndex == 0) {
            return ""; // 第一个条件没有逻辑拼接
        } else {
            if (Objects.isNull(logic)) {
                throw new CompileException("Logic Not Found");
            }
            return " " + logic + " ";
        }
    }

    @Override
    public String getExp() {
        return "boolean " + this.getName() + " = " + exps.stream().map(data -> data.getLogic() + data.getExp()).collect(Collectors.joining()) + ";\n";
    }

    @Override
    public String getName() {
        return "result" + conditionIndex;
    }

    @Override
    public Map<String, String> getParam() {
        Map<String, String> param = new HashMap<>();
        exps.stream().map(CompileInterface::getParam).forEach(data -> {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                param.put(entry.getKey(), entry.getValue());
            }
        });
        return param;
    }
}
