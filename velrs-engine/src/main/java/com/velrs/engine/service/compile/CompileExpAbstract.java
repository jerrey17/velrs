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

        /**
         * TODO 改成这样的表达式 原来的是：NumberExp numberexp0_0 = new NumberExp(vars.get("invoiceAmount"));
         * String var_source_0_0 = vars.get("invoiceAmount");
         * NumberExp numberexp0_0 = new NumberExp(var_source_0_0);// 发票金额
         */

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
        log.info(">> expObj[{}-{}]:{}", this.conditionIndex, this.expIndex, expCode);
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
         * TODO 拆分成多个语句，原来拼装的是：boolean result0_0 = numberexp0_0.notEqual(vars.get("thirdInvoiceAmount"));
         * String var_numberexp0_0 = vars.get("thirdInvoiceAmount");
         * String var_numberexp0_1 = "夜总会";
         * boolean result_numberexp0_0 = numberexp0_0.notEqual(var_target_0_0);
         * boolean result_numberexp0_1 = stringexp0_1.contain(var_target_0_1);
         *
         * String textnumberexp_0_0 = String.format("发票金额(%s) notEqual 三方发票金额(%s) ==> 比对结果:%s", var_source_0_0, var_target_0_0, result0_0);
         * String textnumberexp_0_1 = String.format("发票名称(%s) contain 固定值(%s) ==> 比对结果:%s", var_source_0_1, var_target_0_1, result0_1);
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
            // 有参数
            for (int i = 0; i < targetBeans.size(); i++) {
                sb.append(CompileConstant.VAR_TARGET_NAME_PREFIX).append(i).append(this.getName());
                if (i < targetBeans.size() - 1) {
                    sb.append(", ");
                }
            }
        } else {
            // 无参数 do nothing...  demo：booleanExp0_0.isTrue()
        }
        sb.append(");\n");
        String exp = sb.toString();
        log.info(">> exp[{}-{}]:{}", this.conditionIndex, this.expIndex, exp);

//        * String textnumberexp_0_0 = String.format("发票金额(%s) notEqual 三方发票金额(%s) ==> 比对结果:%s", var_source_0_0, var_target_0_0, result0_0);
//        * String textnumberexp_0_1 = String.format("发票名称(%s) contain 固定值(%s) ==> 比对结果:%s", var_source_0_1, var_target_0_1, result0_1);
        final StringBuffer textSb = new StringBuffer();
        textSb.append("String ").append(CompileConstant.TEXT_NAME_PREFIX).append(this.getName())
                .append(" = ")
                .append("String.format(\"");


        return exp;
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
