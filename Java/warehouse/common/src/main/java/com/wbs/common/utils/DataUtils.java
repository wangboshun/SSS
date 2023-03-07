package com.wbs.common.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author WBS
 * @date 2023/3/4 12:01
 * @desciption DataUtils
 */
public class DataUtils {
    /**
     * 检查是否是数字
     *
     * @param value
     * @return
     */
    public static boolean isNumber(Object value) {
        if (value == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        return pattern.matcher(value.toString()).matches();
    }

    /**
     * Object转List
     */
    public static List<Object> toList(Object obj) {
        List<Object> list = new ArrayList<>();
        if (obj instanceof ArrayList<?>) {
            for (Object o : (List<?>) obj) {
                list.add(o);
            }
            return list;
        }
        return null;
    }

    public static int toInt(String value) {
        return Integer.parseInt(value);
    }

    public static int toInt(Object value) {
        return toInt(value.toString());
    }

    public static float toFloat(String value) {
        return Float.parseFloat(value);
    }

    public static float toFloat(Object value) {
        return toFloat(value.toString());
    }

    public static double toDouble(String value) {
        return Double.parseDouble(value);
    }

    public static double toDouble(Object value) {
        return toDouble(value.toString());
    }

    public static BigDecimal toDecimal(String value) {
        return new BigDecimal(value);
    }

    public static BigDecimal toDecimal(Object value) {
        return toDecimal(value.toString());
    }
}
