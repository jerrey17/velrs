package com.ysf.velrs.engine.service;
import com.ysf.velrs.engine.model.ResultInfo;
import com.ysf.velrs.engine.service.exp.*;
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
${ruleContent}
    }
}