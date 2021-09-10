package com.velrs.engine;

/**
 * @Author rui
 * @Date 2021-08-12 18:21
 **/
public class MainTest {

    public static void main(String[] args) throws Exception {


        String param = "{\"fact\":{\"myPhone\":\"我的电话\",\"phoneNo\":\"我的电话\"},\"ruleId\":\"r01\",\"projectId\":\"p01\"}";
        System.out.println(param);
//
//        SourceBean sourceBean = new SourceBean();
//        sourceBean.setValueType("123");
//        sourceBean.setCode("cardId");
//        sourceBean.setName("卡id");
//        sourceBean.setClassType("StringExp");
//        sourceBean.setMethod("matches");
//        sourceBean.setParamSize(2);
//
//        List<ConditionModel.ExpBean.TargetBean> targetBeans = new ArrayList<>();
//
//        ConditionModel.ExpBean.TargetBean targetBean = new ConditionModel.ExpBean.TargetBean();
//        targetBean.setValueType("value");
//        targetBean.setValue("123123");
//        targetBean.setCode("");
//        targetBean.setName("");
//        targetBean.setClassType("");
//
//        ConditionModel.ExpBean.TargetBean targetBean1 = new ConditionModel.ExpBean.TargetBean();
//        targetBean1.setValueType("prop");
//        targetBean1.setValue("");
//        targetBean1.setCode("bankCard");
//        targetBean1.setName("银行卡");
//        targetBean1.setClassType("");
//        targetBeans.add(targetBean);
//        targetBeans.add(targetBean1);
//
//
//        ConditionModel.ExpBean condition = new ConditionModel.ExpBean();
//        condition.setSource(sourceBean);
//        condition.setTarget(targetBeans);
//        condition.setLogicalExp("||");

//        ConditionModel conditionModel = new ConditionModel();
//        conditionModel.setExps(Lists.newArrayList(condition));
//        conditionModel.setLogicalExp("||");
//
//
//        List<ConditionModel> models = new ArrayList<>();
//        models.add(conditionModel);
//
//        System.out.println(JSON.toJSONString(models));

//        String code = "package com.velrs.engine;\n" +
//                "\n" +
//                "import com.alibaba.fastjson.JSON;\n" +
//                "import ResultInfo;\n" +
//                "import BaseRuleWorker;\n" +
//                "import BooleanExp;\n" +
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
