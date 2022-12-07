package com.zny.common.utils;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author WBS
 * Date:2022/9/1
 * 时间帮助类
 */

public class DateUtils {

    /**
     * 判断是否是日期
     *
     * @param str 变量
     */
    public static boolean isDate(String str) {
        boolean convertSuccess = true;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            format.setLenient(false);
            format.parse(str);
        } catch (Exception e) {
            convertSuccess = false;
        }
        return convertSuccess;
    }

    /**
     * 判断是否是日期
     *
     * @param o 变量
     */
    public static boolean isDate(Object o) {
        return isDate(o.toString());
    }

    public static @NotNull String dateToStr(LocalDateTime date, String format) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return date.format(df);
    }

    public static @NotNull String dateToStr(LocalDateTime date) {
        return dateToStr(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static @NotNull LocalDateTime strToDate(String str, String format) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(str, df);
    }

    public static @NotNull LocalDateTime strToDate(String str) {
        return strToDate(str, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 时间加减处理
     *
     * @param time   时间
     * @param number 数量
     * @param unit   单位:year(年),month(月),day(日),hour(时),minute(分),second(秒)
     * @param symbol 操作符:+ -
     */
    public static LocalDateTime operate(LocalDateTime time, int number, String unit, String symbol) {
        if (symbol.equals("+")) {
            switch (unit) {
                case "year":
                    return time.plusYears(number);
                case "month":
                    return time.plusMonths(number);
                case "day":
                    return time.plusDays(number);
                case "hour":
                    return time.plusHours(number);
                case "minute":
                    return time.plusMinutes(number);
                case "second":
                    return time.plusSeconds(number);
                default:
                    break;
            }
        } else if (symbol.equals("-")) {
            switch (unit) {
                case "year":
                    return time.minusYears(number);
                case "month":
                    return time.minusMonths(number);
                case "day":
                    return time.minusDays(number);
                case "hour":
                    return time.minusHours(number);
                case "minute":
                    return time.minusMinutes(number);
                case "second":
                    return time.minusSeconds(number);
                default:
                    break;
            }
        }
        return null;
    }
}
