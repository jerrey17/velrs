package com.velrs.engine.service.exp;

import com.velrs.engine.exception.RuleExpiredException;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 数字表达式
 *
 * @Author rui
 * @Date 2021-08-14 15:21
 **/
public class NumberExp {

    private BigDecimal num;

    public NumberExp(String data) {
        if (Objects.isNull(data)) {
            throw new RuleExpiredException("NumberExp参数不能为空");
        }
        try {
            this.num = new BigDecimal(data);
        } catch (NumberFormatException e) {
            throw new RuleExpiredException("NumberExp参数必须为数字");
        }
    }

    /**
     * 是否相等
     *
     * @param target
     * @return
     */
    public boolean equal(String target) {
        return this.supplier(() -> num.compareTo(new BigDecimal(target)) == 0);
    }

    /**
     * 不相等
     *
     * @param target
     * @return
     */
    public boolean notEqual(String target) {
        return this.supplier(() -> num.compareTo(new BigDecimal(target)) != 0);
    }

    /**
     * 大于目标值
     *
     * @param target
     * @return
     */
    public boolean greaterThan(String target) {
        return this.supplier(() -> num.compareTo(new BigDecimal(target)) == 1);
    }

    /**
     * 小于目标值
     *
     * @param target
     * @return
     */
    public boolean lessThan(String target) {
        return this.supplier(() -> num.compareTo(new BigDecimal(target)) == -1);
    }

    /**
     * 大于等于目标值
     *
     * @param target
     * @return
     */
    public boolean greaterThanOrEq(String target) {
        return this.supplier(() -> {
            int cmp = num.compareTo(new BigDecimal(target));
            return cmp == 1 || cmp == 0;
        });
    }

    /**
     * 小于等于目标值
     *
     * @param target
     * @return
     */
    public boolean lessThanOrEq(String target) {
        return this.supplier(() -> {
            int cmp = num.compareTo(new BigDecimal(target));
            return cmp == -1 || cmp == 0;
        });
    }

    /**
     * 开区间
     * 大于target1，且小于target2
     *
     * @param target1
     * @param target2
     * @return
     */
    public boolean open(String target1, String target2) {
        return this.supplier(() -> {
            int cmp1 = num.compareTo(new BigDecimal(target1));
            int cmp2 = num.compareTo(new BigDecimal(target2));
            return cmp1 == 1 && cmp2 == -1;
        });
    }

    /**
     * 闭区间
     * 大于等于target1，且小于等于target2
     *
     * @param target1
     * @param target2
     * @return
     */
    public boolean close(String target1, String target2) {
        return this.supplier(() -> {
            int cmp1 = num.compareTo(new BigDecimal(target1));
            int cmp2 = num.compareTo(new BigDecimal(target2));
            return (cmp1 == 1 || cmp1 == 0) && (cmp2 == -1 || cmp2 == 0);
        });
    }

    /**
     * 左开右闭区间
     * 大于target1，且小于等于target2
     *
     * @param target1
     * @param target2
     * @return
     */
    public boolean leftOrightC(String target1, String target2) {
        return this.supplier(() -> {
            int cmp1 = num.compareTo(new BigDecimal(target1));
            int cmp2 = num.compareTo(new BigDecimal(target2));
            return cmp1 == 1 && (cmp2 == -1 || cmp2 == 0);
        });
    }

    /**
     * 左闭右开区间
     * 大于等于target1，且小于target2
     *
     * @param target1
     * @param target2
     * @return
     */
    public boolean leftCrightO(String target1, String target2) {
        return this.supplier(() -> {
            int cmp1 = num.compareTo(new BigDecimal(target1));
            int cmp2 = num.compareTo(new BigDecimal(target2));
            return (cmp1 == 1 || cmp1 == 0) && cmp2 == -1;
        });
    }

    private Boolean supplier(Supplier<Boolean> result) {
        try {
            return result.get();
        } catch (NumberFormatException e) {
            throw new RuleExpiredException("NumberExp#target条件必须为数字");
        }
    }

    public static void main(String[] args) {

        NumberExp numberExp = new NumberExp("100.23");

        System.out.println(numberExp.equal("100.23"));
        System.out.println(numberExp.greaterThan("0.13249374972939"));
        System.out.println(numberExp.lessThan("12382749237492.12377492"));
        System.out.println(numberExp.greaterThanOrEq("100.23"));
        System.out.println(numberExp.lessThanOrEq("11111.23222"));

        System.out.println(numberExp.close("99.11", "100.23"));
        System.out.println(numberExp.close("99.11", "101.23"));

        System.out.println(numberExp.open("99.11", "101.23"));
        System.out.println(numberExp.open("99.11", "100.23"));

        System.out.println(numberExp.leftCrightO("100.23", "101.23"));
        System.out.println(numberExp.leftCrightO("99.11", "100.23"));

        System.out.println(numberExp.leftOrightC("100.23", "101.23"));
        System.out.println(numberExp.leftOrightC("99.11", "100.23"));
    }

}
