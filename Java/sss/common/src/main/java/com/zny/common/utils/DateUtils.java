package com.zny.common.utils;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author WBS
 * Date:2022/9/1
 * 时间帮助类
 */

public class DateUtils {


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
}
