package com.wbs.common.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author WBS
 * @date 2023/2/23 9:42
 * @desciption DateUtils
 */
public class DateUtils {

    public static String dateToStr(LocalDateTime date) {
        return dateToStr(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String dateToStr(LocalDateTime date, String format) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return date.format(df);
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
        return unixToDate(timestamp, false);
    }

    public static LocalDateTime unixToDate(String str) {
        return unixToDate(str, false);
    }

    public static LocalDateTime unixToDate(String str, boolean isUtc) {
        long timestamp = Long.parseLong(str);
        return unixToDate(timestamp, isUtc);
    }

    public static LocalDateTime unixToDate(long timestamp, boolean isUtc) {
        if (isUtc) {
            return Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
        } else {
            return Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(0)).toLocalDateTime();
        }
    }
}
