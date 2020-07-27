package com.vailter.standard;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateOperationTest {
    @Test
    void testCalIntervalDays() {
        // parseToDate方法作用是将String转为LocalDate，略。
        LocalDate date1 = parseToDate("2020-05-12");
        LocalDate date2 = parseToDate("2021-05-13");
        // 计算日期间隔（错误写法）
        // 答案：1天
        // Period其实只能计算同月的天数、同年的月数，不能计算跨月的天数以及跨年的月数。
        int period = Period.between(date1, date2).getDays();
        System.out.println(period);

        // 正确写法
        // 1
        // toEpochDay()：将日期转换成Epoch 天，也就是相对于1970-01-01（ISO）开始的天数，和时间戳是一个道理，时间戳是秒数。显然，该方法是有一定的局限性的。
        long days = date2.toEpochDay() - date1.toEpochDay();
        System.out.println(days);

        // 2
        // 一定要注意一下date1和date2前后顺序：date1 until date2。
        days = date1.until(date2, ChronoUnit.DAYS);
        System.out.println(days);

        // 3（推荐）
        // ChronoUnit：一组标准的日期时间单位。这组单元提供基于单元的访问来操纵日期，时间或日期时间。
        // 这些单元适用于多个日历系统。这是一个最终的、不可变的和线程安全的枚举。
        days = ChronoUnit.DAYS.between(date1, date2);
        System.out.println(days);
        System.out.println("间隔月份：" + ChronoUnit.MONTHS.between(date1, date2));

        // 1582-10-15 和 1582-10-04
         date1 = parseToDate("1582-10-04");
         date2 = parseToDate("1582-10-15");
        System.out.println("（1582-10-04，1582-10-15）间隔天数：" + ChronoUnit.DAYS.between(date1, date2));
    }

    private LocalDate parseToDate(String s) {
        return LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
