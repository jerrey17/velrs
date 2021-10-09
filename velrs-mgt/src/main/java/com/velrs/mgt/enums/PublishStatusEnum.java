package com.velrs.mgt.enums;

import lombok.Getter;

/**
 * @Author rui
 * @Date 2021-08-19 16:05
 **/
@Getter
public enum PublishStatusEnum {

    //规则发布状态：0=待发布;1=已发布;

    PUBLISHING(0, "待发布"),
    PUBLISHED(1, "已发布"),
    ;

    private int code;

    private String desc;

    PublishStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }}
