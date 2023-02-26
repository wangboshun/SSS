package com.wbs.common.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static String dateToStr(LocalDateTime date, String format) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return date.format(df);
    }

    public static String dateToStr(LocalDateTime date) {
        return dateToStr(date, "yyyy-MM-dd HH:mm:ss");
    }


    public static LocalDateTime strToDate(String str) {
        return strToDate(str, "yyyy-MM-dd HH:mm:ss", false);
    }

    public static LocalDateTime strToDate(String str, boolean isUtc) {
        return strToDate(str, "yyyy-MM-dd HH:mm:ss", isUtc);
    }

    public static LocalDateTime strToDate(String str, String format) {
        return strToDate(str, format, false);
    }

    public static LocalDateTime strToDate(String str, String format, boolean isUtc) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        LocalDateTime time = LocalDateTime.parse(str, df);
        if (isUtc) {
            time = time.plusHours(8);
        }
        return time;
    }


    public static LocalDateTime unixToDate(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static LocalDateTime unixToDate(String str) {
        long timestamp = Long.parseLong(str);
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
