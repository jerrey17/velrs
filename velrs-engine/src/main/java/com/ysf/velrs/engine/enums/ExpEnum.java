package com.ysf.velrs.engine.enums;

import com.ysf.velrs.engine.exception.CompileException;
import lombok.Getter;

import java.util.Objects;

/**
 * @Author rui
 * @Date 2021-08-19 16:05
 **/
@Getter
public enum ExpEnum {

    StringExp("StringExp", "字符串表达式"),
    BooleanExp("BooleanExp", "布尔表达式"),

    ;

    private String expClass;

    private String expName;

    ExpEnum(String expClass, String expName) {
        this.expClass = expClass;
        this.expName = expName;
    }

    public static ExpEnum getByExpClass(String expClass) {

        for (ExpEnum value : values()) {
            if(Objects.equals(value.getExpClass(), expClass)) {
                return value;
            }
        }
        throw new CompileException("无法找到对应的表达式，请检查classType");
    }
}
