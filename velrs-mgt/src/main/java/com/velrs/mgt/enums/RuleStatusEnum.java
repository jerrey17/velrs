package com.velrs.mgt.enums;

import lombok.Getter;

/**
 * @Author rui
 * @Date 2021-08-19 16:05
 **/
@Getter
public enum RuleStatusEnum {

    //0:初始（未编辑）;2:编辑中;3:已编译;1:已发布

    INIT(0, "初始化"),
    PUBLISHED(1, "已发布"),
    EDITING(2, "编辑中"),
    COMPILED(3, "已编译"),
    ;

    private int code;

    private String desc;

    RuleStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }}
