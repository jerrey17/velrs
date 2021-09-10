package com.ysf.velrs.engine.service.exp;

import com.ysf.velrs.engine.exception.RuleExpiredException;

import java.util.Arrays;
import java.util.Objects;

/**
 * 字符串表达式对象
 *
 * @Author rui
 * @Date 2021-08-14 15:16
 **/
public class StringExp {

    private String data;

    public StringExp(String data) {
        if (Objects.isNull(data)) {
            throw new RuleExpiredException("StringExp参数不能为空");
        }
        this.data = data;
    }

    /**
     * todo 这是一个测试方法，之后记得删除
     *
     * @param a
     * @param b
     * @return
     */
    public boolean test(String a, String b) {
        return true;
    }

    /**
     * 为空
     *
     * @return
     */
    public boolean isNull() {
        return Objects.isNull(data);
    }

    /**
     * 不为空
     *
     * @return
     */
    public boolean notNull() {
        return !this.isNull();
    }

    /**
     * 是否相等
     *
     * @param target
     * @return
     */
    public boolean equal(String target) {
        if (Objects.isNull(target)) {
            throw new RuleExpiredException("StringExp#equal方法条件不能为空");
        }
        return Objects.equals(data, target);
    }

    /**
     * 不相等
     *
     * @param target
     * @return
     */
    public boolean notEqual(String target) {
        if (Objects.isNull(target)) {
            throw new RuleExpiredException("StringExp#notEqual方法条件不能为空");
        }
        return !Objects.equals(data, target);
    }

    /**
     * 是否以目标字符串开头
     *
     * @param target
     * @return
     */
    public boolean startWith(String target) {
        if (Objects.isNull(target)) {
            throw new RuleExpiredException("StringExp#startWith方法条件不能为空");
        }
        return this.data.startsWith(target);
    }

    /**
     * 是否以目标字符串结尾
     *
     * @param target
     * @return
     */
    public boolean endWith(String target) {
        if (Objects.isNull(target)) {
            throw new RuleExpiredException("StringExp#endWith方法条件不能为空");
        }
        return this.data.endsWith(target);
    }

    /**
     * 正则匹配
     *
     * @param target
     * @return
     */
    public boolean matches(String target) {
        if (Objects.isNull(target)) {
            throw new RuleExpiredException("StringExp#matches方法条件不能为空");
        }
        return this.data.matches(target);
    }

    /**
     * 包含
     *
     * @param target
     * @return
     */
    public boolean contain(String target) {
        if (Objects.isNull(target)) {
            throw new RuleExpiredException("StringExp#contain方法条件不能为空");
        }
        return this.data.contains(target);
    }

    /**
     * 不包含
     *
     * @param target
     * @return
     */
    public boolean notContain(String target) {
        if (Objects.isNull(target)) {
            throw new RuleExpiredException("StringExp#notContain方法条件不能为空");
        }
        return !this.data.contains(target);
    }

    /**
     * 任何一个匹配上
     *
     * @param target 多个匹配参数用英文逗号分隔
     * @return
     */
    public boolean anyMatch(String target) {
        if (Objects.isNull(target)) {
            throw new RuleExpiredException("StringExp#eqAnyOne方法条件不能为空");
        }
        return Arrays.stream(target.split(",")).anyMatch(data -> Objects.equals(data, this.data));
    }


}
