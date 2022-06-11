package com.velrs.engine.service.compile;

import com.velrs.engine.constant.CompileConstant;
import com.velrs.engine.exception.CompileException;
import com.velrs.engine.model.ConditionModel;
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
        final StringBuffer sb = new StringBuffer();
        sb.append("\t\t")
                .append("String ").append(CompileConstant.VAR_SOURCE_NAME_PREFIX).append(this.getName())
                .append(" = vars.get(\"").append(sourceBean.getCode()).append("\");\n");
        sb.append("\t\t")
                .append(clazz.getSimpleName())
                .append(" ").append(CompileConstant.SOURCE_NAME_PREFIX).append(this.getName())
                .append(" = new ").append(clazz.getSimpleName())
                .append("(")
                .append(CompileConstant.VAR_SOURCE_NAME_PREFIX).append(this.getName())
                .append(");\n");

        String expCode = sb.toString();
        log.debug(">> expObj[{}-{}]:{}", this.conditionIndex, this.expIndex, expCode);
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

        /**
         * validate method and param
         */
        if (!Arrays.stream(clazz.getMethods())
                .anyMatch(method -> Objects.equals(method.getName(), sourceBean.getMethod())
                        && method.getParameterCount() == sourceBean.getParamSize())) {
            throw new CompileException(String.format("Source Method Not Found [index:%s,%s]", conditionIndex, expIndex));
        }
        if (Objects.isNull(targetBeans)) {
            if (sourceBean.getParamSize() != 0) {
                throw new CompileException(String.format("Target Size Not Equal Source Param Size [index:%s,%s]", conditionIndex, expIndex));
            }
        } else {
            if (sourceBean.getParamSize() == 0 && targetBeans.size() != 0) {
                throw new CompileException(String.format("Target Size Not Equal Source Param Size [index:%s,%s]", conditionIndex, expIndex));
            }
            if (sourceBean.getParamSize() != exp.getTarget().size()) {
                throw new CompileException(String.format("Target Not Found [index:%s,%s]", conditionIndex, expIndex));
            }
        }

        /**
         * compile the target code
         */
        final StringBuffer sb = new StringBuffer();
        final boolean haveParam = Objects.nonNull(sourceBean) && sourceBean.getParamSize() > 0;
        if (haveParam) {
            // 有参数
            for (int i = 0; i < targetBeans.size(); i++) {
                ConditionModel.ExpBean.TargetBean targetBean = targetBeans.get(i);

                sb.append("\t\t")
                        .append("String ")
                        .append(CompileConstant.VAR_TARGET_NAME_PREFIX).append(i).append(this.getName())
                        .append(" = ");

                if (Objects.equals(targetBean.getValueType(), CompileConstant.VALUE)) {
                    // 值
                    sb.append("\"").append(targetBean.getValue()).append("\";\n");
                } else if (Objects.equals(targetBean.getValueType(), CompileConstant.PROP)) {
                    // 对象
                    sb.append("vars.get(\"").append(targetBean.getCode()).append("\");\n");
                    // 对象是需要调用时传进来的
                    paramsMap.put(targetBean.getCode(), targetBean.getName());
                } else {
                    new CompileException("target valueType not found, expectValue[value, prop]");
                }
            }
        }
        sb.append("\t\t")
                .append("boolean ")
                .append(CompileConstant.RESULT_NAME_PREFIX).append(this.getName())
                .append(" = ")
                .append(CompileConstant.SOURCE_NAME_PREFIX).append(this.getName())
                .append(".").append(sourceBean.getMethod())
                .append("(");
        if (haveParam) {
            // have param
            for (int i = 0; i < targetBeans.size(); i++) {
                sb.append(CompileConstant.VAR_TARGET_NAME_PREFIX).append(i).append(this.getName());
                if (i < targetBeans.size() - 1) {
                    sb.append(", ");
                }
            }
        } else {
            // not param, do nothing...  exp：booleanExp0_0.isTrue()
        }
        sb.append(");\n");
        String exp = sb.toString();
        log.debug(">> exp[{}-{}]:{}", this.conditionIndex, this.expIndex, exp);

        /**
         * compile the rule text
         */
        final String textName = CompileConstant.TEXT_NAME_PREFIX + this.getName();
        final StringBuffer textSb = new StringBuffer();
        textSb.append("\t\t").append("ResultMessage ").append(textName).append(" = new ResultMessage();\n")
                .append("\t\t").append(textName).append(".setCode(\"").append(sourceBean.getCode()).append("\");\n")
                .append("\t\t").append(textName).append(".setName(\"").append(sourceBean.getName()).append("\");\n");
        final StringBuffer targetVal = new StringBuffer();
        final StringBuffer textExp = new StringBuffer();
        textExp.append("String.format(\"(%s)").append(sourceBean.getMethod()).append("(");
        if (haveParam) {
            for (int i = 0; i < targetBeans.size(); i++) {
                textExp.append("%s");
                targetVal.append(CompileConstant.VAR_TARGET_NAME_PREFIX).append(i).append(this.getName());
                if(i != targetBeans.size() - 1) {
                    textExp.append(", ");
                    targetVal.append(", ");
                }
            }
        }
        textExp.append(")\", ")
                .append(CompileConstant.VAR_SOURCE_NAME_PREFIX).append(this.getName()).append(", ")
                .append(targetVal).append(")");
        textSb.append("\t\t").append(textName).append(".setExp(").append(textExp).append(");\n");
        textSb.append("\t\t").append(textName).append(".setResult(").append(CompileConstant.RESULT_NAME_PREFIX).append(this.getName()).append(");\n");
        textSb.append("\t\t").append("resultInfo.addResultMessage(").append(textName).append(");\n\n");
        String text = textSb.toString();

        log.debug(">> exp-text[{}-{}]:{}", this.conditionIndex, this.expIndex, text);
        return exp + text;
    }

    @Override
    public String getName() {
        // name suffix
        return name + conditionIndex + expIndex;
    }

    @Override
    public Map<String, String> getParam() {
        return this.paramsMap;
    }

}
