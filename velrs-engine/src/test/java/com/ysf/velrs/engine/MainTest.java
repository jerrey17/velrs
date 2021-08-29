package com.ysf.velrs.engine;

import com.alibaba.fastjson.JSON;
import com.ysf.velrs.engine.controller.message.CompileReqMessage;
import com.ysf.velrs.engine.model.ConditionModel;
import com.ysf.velrs.engine.model.ConditionModel.ExpBean.SourceBean;
import com.ysf.velrs.engine.service.R_r01_1630222777817;
import com.ysf.velrs.engine.service.compile.CompileHandler;
import org.assertj.core.util.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author rui
 * @Date 2021-08-12 18:21
 **/
public class MainTest {

    public static void main(String[] args) throws Exception {

        R_r01_1630222777817 r = new R_r01_1630222777817();
        Map<String, String> map = new HashMap<>();
        map.put("phoneNo", "15813306492");
        map.put("myPhone", "1");
        System.out.println(JSON.toJSONString(r.run(map)));
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
