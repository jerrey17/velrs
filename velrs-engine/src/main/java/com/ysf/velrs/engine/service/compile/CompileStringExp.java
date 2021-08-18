package com.ysf.velrs.engine.service.compile;

import com.ysf.velrs.engine.model.ConditionModel;
import com.ysf.velrs.engine.service.exp.StringExp;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author rui
 * @Date 2021-08-18 16:38
 **/
@Slf4j
public class CompileStringExp extends CompileExpAbstract implements CompileExpInterface {

    public CompileStringExp(ConditionModel.ConditionsBean condition, int ruleIndex, int conditionIndex) {
        super(condition, ruleIndex, conditionIndex, StringExp.class);
    }

}
