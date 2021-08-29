package com.ysf.velrs.engine.service.compile;

import com.ysf.velrs.engine.controller.message.CompileReqMessage;
import com.ysf.velrs.engine.controller.message.CompileRespMessage;
import com.ysf.velrs.engine.enums.ExpEnum;
import com.ysf.velrs.engine.exception.CompileException;
import com.ysf.velrs.engine.model.ConditionModel;
import com.ysf.velrs.engine.service.exp.BooleanExp;
import com.ysf.velrs.engine.service.exp.StringExp;
import com.ysf.velrs.engine.utils.RuleRunnerUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.*;

/**
 * 编译控制器
 *
 * @Author rui
 * @Date 2021-09-18 16:30
 **/
@Slf4j
@Component
public class CompileHandler {

    @Autowired
    Configuration config;

    /**
     * 编译执行
     *
     * @param message
     * @return
     */
    public CompileRespMessage execute(CompileReqMessage message) {

        long compileTime = System.currentTimeMillis();
        String javaClassName = RuleRunnerUtil.getClassName(message.getRuleId(), compileTime);
        log.info(">>> 编译规则类:{};编译时间:{}", javaClassName, compileTime);

        final Map<String, Object> compileMap = new HashMap();
        compileMap.put("className", javaClassName);
        compileMap.put("compileTime", compileTime);
        compileMap.put("ruleContent", this.getRuleContent(message.getRule()));
        compileMap.put("compiler", message.getOperator());
        compileMap.put("remark", "可执行规则");

        try {
            Template template = config.getTemplate("rule_template.ftl");
            String clazz = FreeMarkerTemplateUtils.processTemplateIntoString(template, compileMap);
            log.info(">>> 编译后的类:{}", clazz);

            CompileRespMessage response = new CompileRespMessage();
            response.setRuleId(message.getRuleId());
            response.setRuleByteCode(new String(Base64.getEncoder().encode(clazz.getBytes())));
            response.setRequestParam(""); // todo
            response.setCompileTime(compileTime);
            return response;
        } catch (IOException e) {
            throw new CompileException("编译异常" + e.getMessage(), e);
        } catch (TemplateException e) {
            throw new CompileException("规则模板异常:" + e.getMessage(), e);
        }


    }

    /**
     * 获取规则内容
     *
     * @param conditionModels
     * @return
     */
    private String getRuleContent(List<ConditionModel> conditionModels) {
        final List<CompileInterface> conditions = new ArrayList<>(conditionModels.size());

        // 循环编译
        for (int i = 0; i < conditionModels.size(); i++) {
            ConditionModel conditionModel = conditionModels.get(i);
            List<ConditionModel.ExpBean> expBeans = conditionModel.getExps();
            final List<CompileInterface> exps = new ArrayList<>(expBeans.size());
            for (int j = 0; j < expBeans.size(); j++) {
                // 编译一个最小单元的表达式
                exps.add(this.getExp(expBeans.get(j), i, j));
            }
            // 编译一整个条件表达式
            conditions.add(new CompileCondition(exps, conditionModel.getLogicalExp(), i));
        }

        // 组装结果
        StringBuffer content = new StringBuffer();
        StringBuffer resultExp = new StringBuffer();
        for (int i = 0; i < conditions.size(); i++) {
            CompileInterface condition = conditions.get(i);

            content.append(condition.getExpObj())   // 条件初始化对象
//                    .append(condition.getLogic())   // 逻辑表达式
                    .append(condition.getExp());    // 条件表达式
            resultExp.append(condition.getLogic())  // 逻辑表达式
                    .append(condition.getName());   // 条件对象名称

        }
        String ruleContent = content.toString() + this.getResultInfo(resultExp.toString());

        log.info(">>> 编译后的ruleContent:{}", ruleContent);

        return ruleContent;
    }

    /**
     * 获取结果信息
     *
     * @param resultExp
     * @return
     */
    private String getResultInfo(String resultExp) {
        return "ResultInfo resultInfo = new ResultInfo();\n" +
                "resultInfo.setPass(" + resultExp + ");\n" +
                "return resultInfo;";
    }

    /**
     * 获取表达式实现类
     *
     * @param exp
     * @param conditionIndex
     * @param expIndex
     * @return
     */
    private CompileInterface getExp(ConditionModel.ExpBean exp, int conditionIndex, int expIndex) {

        ExpEnum expEnum = ExpEnum.getByExpClass(exp.getSource().getClassType());

        switch (expEnum) {
            case StringExp:
                return new CompileStringExp(exp, conditionIndex, expIndex);
            case BooleanExp:
                return new CompileBooleanExp(exp, conditionIndex, expIndex);
        }
        throw new CompileException("不认识的规则表达式");
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
