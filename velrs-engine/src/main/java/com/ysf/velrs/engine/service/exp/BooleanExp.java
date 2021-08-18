package com.ysf.velrs.engine.service.exp;

import java.util.Objects;

/**
 * 布尔表达式
 *
 * @Author rui
 * @Date 2021-08-14 15:20
 **/
public class BooleanExp {

    String data;

    public BooleanExp(String data) {
        this.data = data;
    }

    /**
     * 是否为true
     *
     * @return
     */
    public boolean isTrue() {

        if (Objects.isNull(data)) {
            return false;
        }

        if (Objects.equals(data, "true") || Objects.equals(data, 1)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否为false
     *
     * @return
     */
    public boolean isFalse() {
        return !isTrue();
    }

}
