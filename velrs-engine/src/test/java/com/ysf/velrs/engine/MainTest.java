package com.ysf.velrs.engine;

import java.util.Base64;

/**
 * @Author rui
 * @Date 2021-08-12 18:21
 **/
public class MainTest {

    public static void main(String[] args) {
        String code = "package com.ysf.velrs.engine;\n" +
                "\n" +
                "import com.alibaba.fastjson.JSON;\n" +
                "import com.ysf.velrs.engine.model.ResultInfo;\n" +
                "import com.ysf.velrs.engine.service.BaseRuleWorker;\n" +
                "\n" +
                "import java.util.Map;\n" +
                "\n" +
                "/**\n" +
                " * @Author rui\n" +
                " * @Date 2021-08-12 18:17\n" +
                " **/\n" +
                "public class TestRuleWorker implements BaseRuleWorker {\n" +
                "\n" +
                "    @Override\n" +
                "    public ResultInfo run(Map<String, String> vars) throws Exception {\n" +
                "        ResultInfo resultInfo = new ResultInfo();\n" +
                "        resultInfo.setPass(true);\n" +
                "        resultInfo.setResultMsg(JSON.toJSONString(vars));\n" +
                "        return resultInfo;\n" +
                "    }\n" +
                "}\n";
        System.out.println(new String(Base64.getEncoder().encode(code.getBytes())));
    }
}
