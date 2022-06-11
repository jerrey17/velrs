package com.velrs.engine.service;
import com.velrs.engine.model.ResultInfo;
import com.velrs.engine.model.ResultMessage;
import com.velrs.engine.service.exp.*;
import java.util.Map;
/**
  * ${remark}
  *
  * @Author ${compiler}
  * @Date ${compileTime}
  **/
public class ${className} implements BaseRuleWorker {
    @Override
    public ResultInfo run(Map<String, String> vars) throws Exception {
        ResultInfo resultInfo = new ResultInfo();
${ruleContent}
        return resultInfo;
    }
}