package com.velrs.engine;

import com.velrs.engine.model.ResultInfo;
import com.velrs.engine.service.BaseRuleWorker;
import com.velrs.engine.service.exp.*;
import java.util.Map;
/**
 * 可执行规则
 *
 * @Author 张三
 * @Date 2022-05-01 20:48:15
 **/
public class R_demo1_1651409295860 implements BaseRuleWorker {



    @Override
    public ResultInfo run(Map<String, String> vars) throws Exception {

        String _var_source_numberexp00 = vars.get("invoiceAmount");
        NumberExp _source_numberexp00 = new NumberExp(_var_source_numberexp00);
        String _var_source_stringexp01 = vars.get("invoiceName");
        StringExp _source_stringexp01 = new StringExp(_var_source_stringexp01);

        String _var_target_0numberexp00 = vars.get("thirdInvoiceAmount");
        boolean _result_numberexp00 = _source_numberexp00.notEqual(_var_target_0numberexp00);
        String _var_target_0stringexp01 = "夜总会";
        boolean _result_stringexp01 = _source_stringexp01.contain(_var_target_0stringexp01);

        boolean result0 =  _result_numberexp00 &&  _result_stringexp01;

        String _var_source_numberexp10 = vars.get("invoiceAmount");
        NumberExp _source_numberexp10 = new NumberExp(_var_source_numberexp10);
        String _var_source_dateexp11 = vars.get("invoiceDate");
        DateExp _source_dateexp11 = new DateExp(_var_source_dateexp11);

        String _var_target_0numberexp10 = "100";
        boolean _result_numberexp10 = _source_numberexp10.greaterThan(_var_target_0numberexp10);
        String _var_target_0dateexp11 = vars.get("thirdInvoiceDate");
        String _var_target_1dateexp11 = "3";
        boolean _result_dateexp11 = _source_dateexp11.innerRangeByDay(_var_target_0dateexp11, _var_target_1dateexp11);

        boolean result1 =  _result_numberexp10 &&  _result_dateexp11;

        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setPass(result0 || result1);

        return resultInfo;

//        return null;
//        String _var_numberexp00 = vars.get("invoiceAmount");
//        String _var_numberexp0_1 = vars.get("invoiceName");
//        NumberExp _numberexp0_0 = new NumberExp(_var_numberexp00);// 发票金额
//        StringExp _stringexp0_1 = new StringExp(_var_numberexp0_1);// 发票名称
//
//        String var_target_0_0 = vars.get("thirdInvoiceAmount");
//        String var_target_0_1 = "夜总会";
//        boolean result0_0 = _numberexp0_0.notEqual(var_target_0_0);
//        boolean result0_1 = _stringexp0_1.contain(var_target_0_1);
//        boolean result0 = result0_0 && result0_1;
//
//        String text0_0 = String.format("发票金额(%s) notEqual 三方发票金额(%s) ==> 比对结果:%s", _var_numberexp00, var_target_0_0, result0_0);
//        String text0_1 = String.format("发票名称(%s) contain 固定值(%s) ==> 比对结果:%s", _var_numberexp0_1, var_target_0_1, result0_1);
//
//        // 发票金额(100) equal 三方发票金额(59) ==> 比对失败
//        // 发票名称(是否包含夜总会) contain 固定值(夜总会) ==> 比对成功
//
//        NumberExp numberexp1_0 = new NumberExp(vars.get("invoiceAmount"));
//        DateExp dateexp1_1 = new DateExp(vars.get("invoiceDate"));
//
//        // todo 多个参数的场景
//        boolean result1 = numberexp1_0.greaterThan("100") && dateexp1_1.innerRangeByDay(vars.get("thirdInvoiceDate"), "3");
//
//        ResultInfo resultInfo = new ResultInfo();
//
//        resultInfo.setPass(result0 || result1);
//
//        resultInfo.getRunDetail().add(text0_0);
//        resultInfo.getRunDetail().add(text0_1);

//        return resultInfo;
    }
}