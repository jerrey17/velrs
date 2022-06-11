package com.velrs.engine.model;

import lombok.Data;

/**
 * 结果信息
 *
 * @Author rui
 * @Date 2022-06-03 15:23
 **/
@Data
public class ResultMessage {

    /**
     * code
     */
    private String code;
    /**
     * name
     */
    private String name;
    /**
     * 表达式
     */
    private String exp;
    /**
     * 结果
     */
    private Boolean result;
}
