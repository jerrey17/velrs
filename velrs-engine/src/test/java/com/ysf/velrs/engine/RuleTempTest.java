package com.ysf.velrs.engine;

import com.ysf.velrs.engine.controller.message.CompileReqMessage;
import com.ysf.velrs.engine.model.ConditionModel;
import com.ysf.velrs.engine.service.compile.CompileHandler;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import groovy.util.logging.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author rui
 * @Date 2021-08-29 13:58
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RuleTempTest {

    @Autowired
    Configuration config;

    @Autowired
    private CompileHandler compileHandler;

    @Test
    public void freemarkerTest() {

        try {
            Template template = config.getTemplate("rule_template.ftl");
            Map map = new HashMap();
            map.put("remark", "备注");
            map.put("className", "类名");
            map.put("rule", "StringExp stringexp0_0 = new StringExp(vars.get(\"cardId\"));\n" +
                    "boolean result0 = stringexp0_0.matches(\"123123\", vars.get(\"bankCard\"));        ResultInfo resultInfo = new ResultInfo();\n" +
                    "        resultInfo.setPass(result0);\n" +
                    "        return resultInfo;");
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void compile() {

        ConditionModel.ExpBean.SourceBean sourceBean = new ConditionModel.ExpBean.SourceBean();
        sourceBean.setCode("cardId");
        sourceBean.setName("卡id");
        sourceBean.setClassType("StringExp");
        sourceBean.setMethod("matches");
        sourceBean.setParamSize(2);

        List<ConditionModel.ExpBean.TargetBean> targetBeans = new ArrayList<>();

        ConditionModel.ExpBean.TargetBean targetBean = new ConditionModel.ExpBean.TargetBean();
        targetBean.setValueType("value");
        targetBean.setValue("123123");
        targetBean.setCode("");
        targetBean.setName("");

        ConditionModel.ExpBean.TargetBean targetBean1 = new ConditionModel.ExpBean.TargetBean();
        targetBean1.setValueType("prop");
        targetBean1.setValue("");
        targetBean1.setCode("bankCard");
        targetBean1.setName("银行卡");
        targetBeans.add(targetBean);
        targetBeans.add(targetBean1);


        ConditionModel.ExpBean condition = new ConditionModel.ExpBean();
        condition.setSource(sourceBean);
        condition.setTarget(targetBeans);
        condition.setLogicalExp("||");

        ConditionModel conditionModel = new ConditionModel();
        conditionModel.setExps(Lists.newArrayList(condition));
        conditionModel.setLogicalExp("||");


        List<ConditionModel> models = new ArrayList<>();
        models.add(conditionModel);
        CompileReqMessage message = new CompileReqMessage();
        message.setRuleId("rule1001");
        message.setProjectId("p1001");
        message.setOperator("张三");
        message.setRule(models);

        compileHandler.execute(message);

    }
}
