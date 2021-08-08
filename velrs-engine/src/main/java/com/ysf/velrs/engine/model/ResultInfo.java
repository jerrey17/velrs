package com.ysf.velrs.engine.model;

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
    private String resultMsg;

    public boolean isPass() {
        return isPass;
    }

    public void setPass(boolean pass) {
        isPass = pass;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
}
