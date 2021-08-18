package com.ysf.velrs.engine;

import com.ysf.velrs.engine.model.ConditionModel;
import com.ysf.velrs.engine.model.ConditionModel.ConditionsBean.SourceBean;
import com.ysf.velrs.engine.service.compile.CompileExpAbstract;
import com.ysf.velrs.engine.service.compile.CompileStringExp;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @Author rui
 * @Date 2021-08-12 18:21
 **/
public class MainTest {

    public static void main(String[] args) {

        SourceBean sourceBean = new SourceBean();
        sourceBean.setValueType("123");
        sourceBean.setCode("cardId");
        sourceBean.setName("卡id");
        sourceBean.setClassType("StringExp");
        sourceBean.setMethod("matches");
        sourceBean.setParamSize(2);

        List<ConditionModel.ConditionsBean.TargetBean> targetBeans = new ArrayList<>();

        ConditionModel.ConditionsBean.TargetBean targetBean = new ConditionModel.ConditionsBean.TargetBean();
        targetBean.setValueType("value");
        targetBean.setValue("123123");
        targetBean.setCode("");
        targetBean.setName("");
        targetBean.setClassType("");

        ConditionModel.ConditionsBean.TargetBean targetBean1 = new ConditionModel.ConditionsBean.TargetBean();
        targetBean1.setValueType("prop");
        targetBean1.setValue("");
        targetBean1.setCode("bankCard");
        targetBean1.setName("银行卡");
        targetBean1.setClassType("");
        targetBeans.add(targetBean);
        targetBeans.add(targetBean1);


        ConditionModel.ConditionsBean condition = new ConditionModel.ConditionsBean();
        condition.setSource(sourceBean);
        condition.setTarget(targetBeans);
        condition.setLogicalExp("||");

        CompileStringExp stringExp = new CompileStringExp(condition, 0, 0);

        System.out.println(stringExp.getExpObj());
        System.out.println(stringExp.getLogic());
        System.out.println(stringExp.getExp());

//        String code = "package com.ysf.velrs.engine;\n" +
//                "\n" +
//                "import com.alibaba.fastjson.JSON;\n" +
//                "import com.ysf.velrs.engine.model.ResultInfo;\n" +
//                "import com.ysf.velrs.engine.service.BaseRuleWorker;\n" +
//                "import com.ysf.velrs.engine.service.exp.BooleanExp;\n" +
//                "\n" +
//                "import java.util.Map;\n" +
//                "\n" +
//                "/**\n" +
//                " * @Author rui\n" +
//                " * @Date 2021-08-12 18:17\n" +
//                " **/\n" +
//                "public class TestRuleWorker implements BaseRuleWorker {\n" +
//                "\n" +
//                "    @Override\n" +
//                "    public ResultInfo run(Map<String, String> vars) throws Exception {\n" +
//                "\n" +
//                "        BooleanExp booleanExp = new BooleanExp(vars.get(\"sex\"));\n" +
//                "\n" +
//                "        ResultInfo resultInfo = new ResultInfo();\n" +
//                "        resultInfo.setPass(booleanExp.isTrue());\n" +
//                "        resultInfo.setResultMsg(JSON.toJSONString(vars));\n" +
//                "        return resultInfo;\n" +
//                "    }\n" +
//                "}\n";
//        System.out.println(new String(Base64.getEncoder().encode(code.getBytes())));
    }
}
