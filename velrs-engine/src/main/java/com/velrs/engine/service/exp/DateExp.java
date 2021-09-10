package com.velrs.engine.service.exp;

import com.velrs.engine.exception.RuleExpiredException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Supplier;

/**
 * 日期表达式
 *
 * @Author rui
 * @Date 2021-08-14 15:20
 **/
public class DateExp {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private LocalDateTime data;

    public DateExp(String data) {
        try {
            this.data = LocalDateTime.parse(data, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new RuleExpiredException("DateExp参数格式异常，支持的日期格式:yyyy-MM-dd HH:mm:ss");
        }
    }

    /**
     * source与target相等
     *
     * @param target
     * @return
     */
    public boolean equal(String target) {
        return this.supplier(() -> this.data.equals(LocalDateTime.parse(target, DATE_TIME_FORMATTER)));
    }

    /**
     * source与target不相等
     *
     * @param target
     * @return
     */
    public boolean notEqual(String target) {
        return this.supplier(() -> !this.data.equals(LocalDateTime.parse(target, DATE_TIME_FORMATTER)));
    }


    /**
     * source在target日期之后
     *
     * @param target
     * @return
     */
    public boolean isAfter(String target) {
        return this.supplier(() -> this.data.isAfter(LocalDateTime.parse(target, DATE_TIME_FORMATTER)));
    }

    /**
     * source在target日期之前
     *
     * @param target
     * @return
     */
    public boolean isBefore(String target) {
        return this.supplier(() -> this.data.isBefore(LocalDateTime.parse(target, DATE_TIME_FORMATTER)));
    }

    /**
     * 在两个日期之间
     *
     * @param target1
     * @param target2
     * @return
     */
    public boolean between(String target1, String target2) {
        return this.supplier(() -> {
            LocalDateTime l1 = LocalDateTime.parse(target1, DATE_TIME_FORMATTER);
            LocalDateTime l2 = LocalDateTime.parse(target2, DATE_TIME_FORMATTER);
            // todo
//            data.compareTo()
            return true;

        });

    }


    private Boolean supplier(Supplier<Boolean> result) {
        try {
            return result.get();
        } catch (DateTimeParseException e) {
            throw new RuleExpiredException("DateExp#target条件日期格式异常，支持的日期格式:yyyy-MM-dd HH:mm:ss");
        }
    }

    public static void main(String[] args) {
        DateExp dateExp = new DateExp("2021-09-12 23:49:32");


        LocalDateTime localDateTime = LocalDateTime.parse("2021-09-12 23:49:32", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime target1 = LocalDateTime.parse("2021-09-11 23:49:32", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        System.out.println(localDateTime.isEqual(target1));
    }


}
