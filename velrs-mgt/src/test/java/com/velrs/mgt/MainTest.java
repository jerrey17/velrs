package com.velrs.mgt;

import com.alibaba.fastjson.JSON;
import com.velrs.mgt.controller.message.CreateReqMessage;
import com.velrs.mgt.controller.message.SaveCompileResultReqMessage;

/**
 * @Author rui
 * @Date 2021-10-01 18:21
 **/
public class MainTest {

    public static void main(String[] args) throws Exception {

        SaveCompileResultReqMessage message = new SaveCompileResultReqMessage();
        message.setRuleId("");
        message.setRuleByteCode("");
        message.setRequestParam("");
        message.setCompileTime(0L);
        message.setCompiler(0L);


        System.out.println(JSON.toJSONString(message));


    }


}
