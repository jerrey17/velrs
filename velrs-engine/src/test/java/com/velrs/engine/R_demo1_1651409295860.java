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
        ResultInfo resultInfo = new ResultInfo();
        String _var_source_numberexp00 = vars.get("invoiceAmount");
        NumberExp _source_numberexp00 = new NumberExp(_var_source_numberexp00);
        String _var_source_stringexp01 = vars.get("invoiceName");
        StringExp _source_stringexp01 = new StringExp(_var_source_stringexp01);
        String _var_target_0numberexp00 = vars.get("thirdInvoiceAmount");
        boolean _result_numberexp00 = _source_numberexp00.notEqual(_var_target_0numberexp00);
        String _text_numberexp00 = String.format("发票金额[invoiceAmount]:(%s)notEqual(%s) ==> 比对结果:%s", _var_source_numberexp00, _var_target_0numberexp00, _result_numberexp00);
        resultInfo.addResultMessage(_text_numberexp00);

        String _var_target_0stringexp01 = "夜总会";
        boolean _result_stringexp01 = _source_stringexp01.contain(_var_target_0stringexp01);
        String _text_stringexp01 = String.format("发票名称[invoiceName]:(%s)contain(%s) ==> 比对结果:%s", _var_source_stringexp01, _var_target_0stringexp01, _result_stringexp01);
        resultInfo.addResultMessage(_text_stringexp01);

        boolean result0 = _result_numberexp00 && _result_stringexp01;
        String _var_source_numberexp10 = vars.get("invoiceAmount");
        NumberExp _source_numberexp10 = new NumberExp(_var_source_numberexp10);
        String _var_source_dateexp11 = vars.get("invoiceDate");
        DateExp _source_dateexp11 = new DateExp(_var_source_dateexp11);
        String _var_target_0numberexp10 = "100";
        boolean _result_numberexp10 = _source_numberexp10.greaterThan(_var_target_0numberexp10);
        String _text_numberexp10 = String.format("发票金额[invoiceAmount]:(%s)greaterThan(%s) ==> 比对结果:%s", _var_source_numberexp10, _var_target_0numberexp10, _result_numberexp10);
        resultInfo.addResultMessage(_text_numberexp10);

        String _var_target_0dateexp11 = vars.get("thirdInvoiceDate");
        String _var_target_1dateexp11 = "3";
        boolean _result_dateexp11 = _source_dateexp11.innerRangeByDay(_var_target_0dateexp11, _var_target_1dateexp11);
        String _text_dateexp11 = String.format("发票日期[invoiceDate]:(%s)innerRangeByDay(%s, %s) ==> 比对结果:%s", _var_source_dateexp11, _var_target_0dateexp11, _var_target_1dateexp11, _result_dateexp11);
        resultInfo.addResultMessage(_text_dateexp11);

        boolean result1 = _result_numberexp10 && _result_dateexp11;
        resultInfo.setPass(result0 || result1);

        return resultInfo;
    }
}