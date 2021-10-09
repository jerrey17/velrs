package com.velrs.mgt.enums;

import lombok.Getter;

/**
 * @Author rui
 * @Date 2021-08-19 16:05
 **/
@Getter
public enum TestStatusEnum {

    //测试状态：0：待测试；1：已测试

    TESTING(0, "待测试"),
    TESTED(1, "已测试"),
    ;

    private int code;

    private String desc;

    TestStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }}
