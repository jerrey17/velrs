package com.ysf.velrs.engine.service.compile;

import com.ysf.velrs.engine.exception.CompileException;
import com.ysf.velrs.engine.model.ConditionModel;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Author rui
 * @Date 2021-08-18 17:47
 **/
@Slf4j
public abstract class CompileExpAbstract<T extends Class> implements CompileExpInterface {

    protected ConditionModel.ConditionsBean condition;
    protected int ruleIndex;
    protected int conditionIndex;
    private String name; // 编译属性的名称
    private T clazz;

    public CompileExpAbstract(ConditionModel.ConditionsBean condition, int ruleIndex, int conditionIndex, T clazz) {
        this.condition = condition;
        this.ruleIndex = ruleIndex;
        this.conditionIndex = conditionIndex;
        this.clazz = clazz;
        this.name = clazz.getSimpleName().toLowerCase();
    }

    @Override
    public String getExpObj() {
        ConditionModel.ConditionsBean.SourceBean sourceBean = condition.getSource();

        if (!Objects.equals(clazz.getSimpleName(), sourceBean.getClassType())) {
            throw new CompileException("Source ClassType Not Found");
        }

        StringBuffer sb = new StringBuffer();
        sb.append(sourceBean.getClassType())
                .append(" ")
                .append(name)
                .append(ruleIndex)
                .append("_")
                .append(conditionIndex)
                .append(" = new ").append(clazz.getSimpleName()).append("(vars.get(\"")
                .append(sourceBean.getCode())
                .append("\"));\n");
        String expCode = sb.toString();
        log.info(">> expObj:{}", expCode);
        return expCode;
    }

    @Override
    public String getLogic() {
        return Objects.nonNull(condition.getLogicalExp()) ? (" " + condition.getLogicalExp() + " ") : "";
    }

    @Override
    public String getExp() {
        ConditionModel.ConditionsBean.SourceBean sourceBean = condition.getSource();

        if (!Arrays.stream(clazz.getMethods()).anyMatch(method -> Objects.equals(method.getName(), sourceBean.getMethod()))) {
            throw new CompileException("Source Method Not Found");
        }

        List<ConditionModel.ConditionsBean.TargetBean> targetBeans = condition.getTarget();
        StringBuffer sb = new StringBuffer();
        sb.append(name)
                .append(ruleIndex)
                .append("_")
                .append(conditionIndex)
                .append(".")
                .append(sourceBean.getMethod())
                .append("(");
        if (sourceBean.getParamSize() > 0) {
            // 有参数
            for (int i = 0; i < targetBeans.size(); i++) {
                ConditionModel.ConditionsBean.TargetBean targetBean = targetBeans.get(i);

                if (Objects.equals(targetBean.getValueType(), "value")) {
                    // 值 stringExp0_1.contain("123"); =》 "123"
                    sb.append("\"").append(targetBean.getValue()).append("\"");

                } else if (Objects.equals(targetBean.getValueType(), "prop")) {
                    // 对象 stringExp0_1.contain(vars.get("sex")) =》 vars.get("sex")
                    sb.append("vars.get(\"").append(targetBean.getCode()).append("\")");
                } else {
                    new CompileException("target valueType not found, expectValue[value, prop]");
                }
                if (i < targetBeans.size() - 1) {
                    sb.append(", ");
                }
            }
        } else {
            // 无参数：booleanExp0_0.isTrue()
        }
        sb.append(")");
        String exp = sb.toString();
        log.info(">> exp:{}", exp);
        return exp;
    }
}
