package com.ysf.velrs.engine.service.compile;

import com.ysf.velrs.engine.controller.message.CompileReqMessage;
import com.ysf.velrs.engine.model.ConditionModel;
import com.ysf.velrs.engine.service.exp.StringExp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 编译控制器
 *
 * @Author rui
 * @Date 2021-08-18 16:30
 **/
@Slf4j
@Component
public class CompileHandler {

    public void execute(CompileReqMessage message) {

        CompileExpInterface compileExpInterface = new CompileStringExp(null, 0, 0);
    }

    /**
     * 字符串编译
     */
    public static class CompileStringExp extends CompileExpAbstract {

        public CompileStringExp(ConditionModel.ConditionsBean condition, int ruleIndex, int conditionIndex) {
            super(condition, ruleIndex, conditionIndex, StringExp.class);
        }
    }
}
