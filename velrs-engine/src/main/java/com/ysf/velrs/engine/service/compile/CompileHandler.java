package com.ysf.velrs.engine.service.compile;

import com.ysf.velrs.engine.controller.message.CompileReqMessage;
import com.ysf.velrs.engine.enums.ExpEnum;
import com.ysf.velrs.engine.model.ConditionModel;
import com.ysf.velrs.engine.service.exp.BooleanExp;
import com.ysf.velrs.engine.service.exp.StringExp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 编译控制器
 *
 * @Author rui
 * @Date 2021-09-18 16:30
 **/
@Slf4j
@Component
public class CompileHandler {

    public void execute(CompileReqMessage message) {

        List<ConditionModel> conditionModels = message.getRule();
        final List<CompileInterface> conditions = new ArrayList<>(conditionModels.size());

        for (int i = 0; i < conditionModels.size(); i++) {

            ConditionModel conditionModel = message.getRule().get(i);
            List<ConditionModel.ExpBean> expBeans = conditionModel.getExps();

            final List<CompileInterface> exps = new ArrayList<>(expBeans.size());
            for (int j = 0; j < expBeans.size(); j++) {
                exps.add(this.getExp(expBeans.get(j), i, j));
            }

            conditions.add(new CompileCondition(exps, conditionModel.getLogicalExp(), i));
        }

        String code = conditions.stream().map(data -> {

            return data.getExpObj() + data.getExp() + data.getLogic();
        }).collect(Collectors.joining());

        log.info("编译测试结果：{}", code);
    }

    private CompileInterface getExp(ConditionModel.ExpBean exp, int conditionIndex, int expIndex) {

        ExpEnum expEnum = ExpEnum.getByExpClass(exp.getSource().getClassType());

        switch (expEnum) {
            case StringExp:
                return new CompileStringExp(exp, conditionIndex, expIndex);
            case BooleanExp:
                return new CompileBooleanExp(exp, conditionIndex, expIndex);

        }

        return null;
    }


    /**
     * 字符串编译
     */
    public static class CompileStringExp extends CompileExpAbstract {
        public CompileStringExp(ConditionModel.ExpBean exp, int conditionIndex, int expIndex) {
            super(exp, conditionIndex, expIndex, StringExp.class);
        }
    }

    /**
     * 布尔表达式编译
     */
    public static class CompileBooleanExp extends CompileExpAbstract {
        public CompileBooleanExp(ConditionModel.ExpBean exp, int conditionIndex, int expIndex) {
            super(exp, conditionIndex, expIndex, BooleanExp.class);
        }
    }
}
