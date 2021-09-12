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
    private LocalDateTime source;

    public DateExp(String source) {
        try {
            this.source = LocalDateTime.parse(source, DATE_TIME_FORMATTER);
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
        return this.supplier(() -> this.source.equals(LocalDateTime.parse(target, DATE_TIME_FORMATTER)));
    }

    /**
     * source与target不相等
     *
     * @param target
     * @return
     */
    public boolean notEqual(String target) {
        return this.supplier(() -> !this.source.equals(LocalDateTime.parse(target, DATE_TIME_FORMATTER)));
    }


    /**
     * source在target日期之后
     *
     * @param target
     * @return
     */
    public boolean isAfter(String target) {
        return this.supplier(() -> this.source.isAfter(LocalDateTime.parse(target, DATE_TIME_FORMATTER)));
    }

    /**
     * source在target日期之前
     *
     * @param target
     * @return
     */
    public boolean isBefore(String target) {
        return this.supplier(() -> this.source.isBefore(LocalDateTime.parse(target, DATE_TIME_FORMATTER)));
    }

    /**
     * 在两个日期之间
     * 闭区间[target1, target2]
     *
     * @param target1
     * @param target2
     * @return
     */
    public boolean between(String target1, String target2) {
        return this.supplier(() -> {
            LocalDateTime l1 = LocalDateTime.parse(target1, DATE_TIME_FORMATTER);
            LocalDateTime l2 = LocalDateTime.parse(target2, DATE_TIME_FORMATTER);
            int left = this.source.compareTo(l1);
            int right = this.source.compareTo(l2);
            return left >= 0 && right <= 0;

        });
    }

    /**
     * source 与 target的误差再指定范围内，单位天
     *
     * @param day
     * @return
     */
    public boolean innerRangeByDay(String target, int day) {
        return this.supplier(() -> {
            LocalDateTime tarTime = LocalDateTime.parse(target, DATE_TIME_FORMATTER);
            LocalDateTime min = this.source.minusDays(day);
            LocalDateTime max = this.source.plusDays(day);
            int left = tarTime.compareTo(min);
            int right = tarTime.compareTo(max);
            return left >= 0 && right <= 0;
        });
    }

    /**
     * source 与 target的误差再指定范围内，单位分钟
     *
     * @param minute
     * @return
     */
    public boolean innerRangeByMin(String target, int minute) {
        return this.supplier(() -> {
            LocalDateTime tarTime = LocalDateTime.parse(target, DATE_TIME_FORMATTER);
            LocalDateTime min = this.source.minusMinutes(minute);
            LocalDateTime max = this.source.plusMinutes(minute);
            int left = tarTime.compareTo(min);
            int right = tarTime.compareTo(max);
            return left >= 0 && right <= 0;
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
//        DateExp dateExp = new DateExp("2021-09-12 23:49:32");
//
//        LocalDateTime localDateTime = LocalDateTime.parse("2021-09-12 23:49:32", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        LocalDateTime target1 = LocalDateTime.parse("2021-09-11 23:49:32", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        LocalDateTime target2 = LocalDateTime.parse("2021-09-13 23:49:32", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        LocalDateTime target3 = LocalDateTime.parse("2021-09-12 23:49:32", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//
////        [target1, tartget2]
////        System.out.println(localDateTime.isEqual(target1));
//        System.out.println(localDateTime.compareTo(target1));
//        System.out.println(localDateTime.compareTo(target2));
//        System.out.println(localDateTime.compareTo(target3));

        DateExp dateExp = new DateExp("2021-09-12 23:49:32");

        System.out.println(dateExp.isAfter("2021-09-12 23:49:31"));
        System.out.println(dateExp.isBefore("2021-09-12 23:49:33"));
        System.out.println(dateExp.between("2021-09-01 23:49:32", "2021-09-20 23:49:32"));
        System.out.println(dateExp.between("2021-09-12 23:49:32", "2021-09-13 23:49:32"));
        System.out.println(dateExp.between("2021-09-11 23:49:32", "2021-09-12 23:49:32"));
        System.out.println(dateExp.innerRangeByDay("2021-09-12 01:49:32", 1));
        System.out.println(dateExp.innerRangeByMin("2021-09-12 23:50:32", 10));


    }


}
