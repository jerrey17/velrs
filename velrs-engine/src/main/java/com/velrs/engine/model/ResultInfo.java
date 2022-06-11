package com.velrs.engine.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 结果信息
 *
 * @Author rui
 * @Date 2021-08-08 15:41
 **/
public class ResultInfo {

    /**
     * 规则运行是否通过
     * true:通过；false:不通过
     *
     * @return
     */
    private boolean isPass;

    /**
     * 结果信息
     */
    @Deprecated
    private String resultMsg;

    /**
     * 结果信息列表
     */
    private List<ResultMessage> resultMessages = new ArrayList<>();

    public boolean isPass() {
        return isPass;
    }

    public void setPass(boolean pass) {
        isPass = pass;
    }

    @Deprecated
    public String getResultMsg() {
        return resultMsg;
    }
    @Deprecated
    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public List<ResultMessage> getResultMessages() {
        return resultMessages;
    }

    public void addResultMessage(ResultMessage resultMessage) {
        resultMessages.add(resultMessage);
    }
}
