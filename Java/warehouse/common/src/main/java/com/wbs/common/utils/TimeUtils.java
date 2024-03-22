package com.wbs.common.utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author WBS
 * @date 2023/2/23 9:42
 * @desciption DateUtils
 */
public class TimeUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_MILLISECOND = "yyyy-MM-dd HH:mm:ss.SSS";

    public static String getDateFormat(String val) {
        String format = TimeUtils.DATE_FORMAT;
        // 只有T
        if (val.contains("T") && !val.contains("Z") && !val.contains(".")) {
            format = "yyyy-MM-dd'T'HH:mm:ss";
        }
        // 有T、有Z
        else if (val.contains("T") && val.contains("Z") && !val.contains(".")) {
            format = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        }
        // 有T、有毫秒
        else if (val.contains("T") && !val.contains("Z") && val.contains(".")) {
            format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        }
        // 有T、有Z、有毫秒
        else if (val.contains("T") && val.contains("Z") && val.contains(".")) {
            format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        }
        // 只有毫秒
        else if (!val.contains("T") && !val.contains("Z") && val.contains(".")) {
            format = DATE_FORMAT_MILLISECOND;
        }
        // 数据库里面秒为00的时候，把秒的格式删除掉
        if (val.contains(":") && (val.split(":").length < 3)) {
            format = format.replace(":ss", "");

        }
        return format;
    }

    public static String dateToStr(LocalDateTime date) {
        return dateToStr(date, DATE_FORMAT);
    }

    public static String dateToStr(LocalDateTime date, String format) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return date.format(df);
    }

    public static LocalDateTime strToDate(String str) {
        return strToDate(str, DATE_FORMAT, false);
    }


    public static LocalDateTime strToDate(String str, boolean isUtc) {
        return strToDate(str, DATE_FORMAT, isUtc);
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

    public static Timestamp strToTimestamp(String str) {
        return strToTimestamp(str, DATE_FORMAT);
    }

    public static Timestamp strToTimestamp(String str, String format) {
        LocalDateTime localDateTime = TimeUtils.strToDate(str, format);
        return Timestamp.valueOf(localDateTime);
    }

    public static String timestampToStr(Timestamp timestamp) {
        return timestampToStr(timestamp, DATE_FORMAT);
    }

    public static String timestampToStr(Timestamp timestamp, String format) {
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        return TimeUtils.dateToStr(localDateTime, format);
    }
}
