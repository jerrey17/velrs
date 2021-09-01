package com.ysf.velrs.engine.service.compile;

import com.ysf.velrs.engine.exception.CompileException;
import com.ysf.velrs.engine.model.ConditionModel;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 表达式编译器
 *
 * @Author rui
 * @Date 2021-08-18 17:47
 **/
@Slf4j
public abstract class CompileExpAbstract<T extends Class> implements CompileInterface {

    protected ConditionModel.ExpBean exp;
    protected int conditionIndex;
    protected int expIndex;
    private String name; // 编译属性的名称
    private T clazz;
    private Map<String, String> paramsMap;

    public CompileExpAbstract(ConditionModel.ExpBean exp, int conditionIndex, int expIndex, T clazz) {
        this.exp = exp;
        this.conditionIndex = conditionIndex;
        this.expIndex = expIndex;
        this.clazz = clazz;
        this.name = clazz.getSimpleName().toLowerCase();
        this.paramsMap = new HashMap<>();
    }

    @Override
    public String getExpObj() {
        ConditionModel.ExpBean.SourceBean sourceBean = exp.getSource();

        if (!Objects.equals(clazz.getSimpleName(), sourceBean.getClassType())) {
            throw new CompileException(String.format("Source ClassType Not Found [index:%s,%s]", conditionIndex, expIndex));
        }

        StringBuffer sb = new StringBuffer();
        sb.append("\t\t").append(sourceBean.getClassType())
                .append(" ")
                .append(this.getName())
                .append(" = new ").append(clazz.getSimpleName()).append("(vars.get(\"")
                .append(sourceBean.getCode())
                .append("\"));\n");
        String expCode = sb.toString();
        log.info(">> expObj:{}", expCode);
        paramsMap.put(sourceBean.getCode(), sourceBean.getName()); // source是传值对象，需要作为参数条件传进来的。
        return expCode;
    }

    @Override
    public String getLogic() {
        if (expIndex == 0) {
            return ""; // 第一个条件没有逻辑拼接
        } else {
            if (Objects.isNull(exp.getLogicalExp())) {
                throw new CompileException(String.format("LogicExp Not Found [index:%s,%s]", conditionIndex, expIndex));
            }
            return " " + exp.getLogicalExp() + " ";
        }
    }

    @Override
    public String getExp() {
        ConditionModel.ExpBean.SourceBean sourceBean = exp.getSource();
        List<ConditionModel.ExpBean.TargetBean> targetBeans = exp.getTarget();

        if (!Arrays.stream(clazz.getMethods())
                .anyMatch(method -> Objects.equals(method.getName(), sourceBean.getMethod())
                        && method.getParameterCount() == sourceBean.getParamSize())) {
            throw new CompileException(String.format("Source Method Not Found [index:%s,%s]", conditionIndex, expIndex));
        }

        if(Objects.isNull(targetBeans)) {
            if(sourceBean.getParamSize() != 0) {
                throw new CompileException(String.format("Target Size Not Equal Source Param Size [index:%s,%s]", conditionIndex, expIndex));
            }
        } else {
            if(sourceBean.getParamSize() == 0 && targetBeans.size() != 0) {
                throw new CompileException(String.format("Target Size Not Equal Source Param Size [index:%s,%s]", conditionIndex, expIndex));
            }
            if(sourceBean.getParamSize() != exp.getTarget().size()) {
                throw new CompileException(String.format("Target Not Found [index:%s,%s]", conditionIndex, expIndex));
            }
        }

        StringBuffer sb = new StringBuffer();
        sb.append(this.getName())
                .append(".")
                .append(sourceBean.getMethod())
                .append("(");
        if (Objects.nonNull(sourceBean) && sourceBean.getParamSize() > 0) {
            // 有参数
            for (int i = 0; i < targetBeans.size(); i++) {
                ConditionModel.ExpBean.TargetBean targetBean = targetBeans.get(i);

                if (Objects.equals(targetBean.getValueType(), "value")) {
                    // 值 stringExp0_1.contain("123"); =》 "123"
                    sb.append("\"").append(targetBean.getValue()).append("\"");

                } else if (Objects.equals(targetBean.getValueType(), "prop")) {
                    // 对象 stringExp0_1.contain(vars.get("sex")) =》 vars.get("sex")
                    sb.append("vars.get(\"").append(targetBean.getCode()).append("\")");
                    // 对象是需要调用时传进来的。
                    paramsMap.put(targetBean.getCode(), targetBean.getName());
                } else {
                    new CompileException("target valueType not found, expectValue[value, prop]");
                }
                if (i < targetBeans.size() - 1) {
                    sb.append(", ");
                }
            }
        } else {
            // 无参数 do nothing...  demo：booleanExp0_0.isTrue()
        }
        sb.append(")");
        String exp = sb.toString();
        log.info(">> exp:{}", exp);
        return exp;
    }

    @Override
    public String getName() {
        return name + conditionIndex + "_" + expIndex;
    }

    @Override
    public Map<String, String> getParam() {
        return this.paramsMap;
    }

}
