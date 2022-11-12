package com.zny.common.utils;

import java.time.LocalDateTime;

/**
 * @author WBS
 * Date 2022-11-12 11:04
 * ObjectUtils
 */

public class DataUtils {

    /**
     * 数值对比
     *
     * @param value        数值
     * @param compareValue 比较值
     * @param symbol       比较符号
     */
    public static boolean compare(Object value, Object compareValue, String symbol) {
        //String类型
        if (value.getClass().isAssignableFrom(String.class)) {
            return stringCompare(value, compareValue, symbol);
        }
        //Integer类型
        else if (value.getClass().isAssignableFrom(Integer.class)) {
            return intCompare(value, compareValue, symbol);
        }
        //Double类型
        else if (value.getClass().isAssignableFrom(Double.class)) {
            return doubleCompare(value, compareValue, symbol);
        }
        //Float类型
        else if (value.getClass().isAssignableFrom(Float.class)) {
            return floatCompare(value, compareValue, symbol);
        }
        //LocalDateTime类型
        else if (value.getClass().isAssignableFrom(LocalDateTime.class)) {
            return dateCompare(value, compareValue, symbol);
        }
        return false;
    }

    /**
     * double类型数值比较
     *
     * @param value        数值
     * @param compareValue 比较值
     * @param symbol       比较符号
     */
    public static boolean doubleCompare(Object value, Object compareValue, String symbol) {
        double value1 = Double.parseDouble(value.toString());
        double value2 = Double.parseDouble(compareValue.toString());
        switch (symbol) {
            case "==":
                return value1 == value2;
            case "!=":
                return value1 != value2;
            case ">":
                return value1 > value2;
            case "<":
                return value1 < value2;
            case ">=":
                return value1 >= value2;
            case "<=":
                return value1 <= value2;
            default:
                break;
        }
        return false;
    }

    /**
     * double类型数值比较
     *
     * @param value        数值
     * @param compareValue 比较值
     * @param symbol       比较符号
     */
    public static boolean floatCompare(Object value, Object compareValue, String symbol) {
        float value1 = Float.parseFloat(value.toString());
        float value2 = Float.parseFloat(compareValue.toString());
        switch (symbol) {
            case "==":
                return value1 == value2;
            case "!=":
                return value1 != value2;
            case ">":
                return value1 > value2;
            case "<":
                return value1 < value2;
            case ">=":
                return value1 >= value2;
            case "<=":
                return value1 <= value2;
            default:
                break;
        }
        return false;
    }

    /**
     * int类型数值比较
     *
     * @param value        数值
     * @param compareValue 比较值
     * @param symbol       比较符号
     */
    public static boolean intCompare(Object value, Object compareValue, String symbol) {
        int value1 = Integer.parseInt(value.toString());
        int value2 = Integer.parseInt(compareValue.toString());
        switch (symbol) {
            case "==":
                return value1 == value2;
            case "!=":
                return value1 != value2;
            case ">":
                return value1 > value2;
            case "<":
                return value1 < value2;
            case ">=":
                return value1 >= value2;
            case "<=":
                return value1 <= value2;
            default:
                break;
        }
        return false;
    }

    /**
     * LocalDateTime类型数值比较
     *
     * @param value        数值
     * @param compareValue 比较值
     * @param symbol       比较符号
     */
    public static boolean dateCompare(Object value, Object compareValue, String symbol) {
        LocalDateTime value1 = DateUtils.strToDate(value.toString());
        LocalDateTime value2 = DateUtils.strToDate(compareValue.toString());
        switch (symbol) {
            case "==":
                return value1.equals(value2);
            case "!=":
                return value1 != value2;
            case ">":
                return value1.isAfter(value2);
            case "<":
                return value1.isBefore(value2);
            case ">=":
                return (value1.equals(value2) || value1.isAfter(value2));
            case "<=":
                return (value1.equals(value2) || value1.isBefore(value2));
            default:
                break;
        }
        return false;
    }

    /**
     * string类型数值比较
     *
     * @param value        数值
     * @param compareValue 比较值
     * @param symbol       比较符号
     */
    public static boolean stringCompare(Object value, Object compareValue, String symbol) {
        String value1 = value.toString();
        String value2 = compareValue.toString();
        if ("==".equals(symbol)) {
            return value1.equals(value2);
        } else if ("!=".equals(symbol)) {
            return !value1.equals(value2);
        } else if (symbol.contains("int")) {
            return intCompare(value, compareValue, symbol.replace("int", ""));
        } else if (symbol.contains("float")) {
            return floatCompare(value, compareValue, symbol.replace("float", ""));
        } else if (symbol.contains("double")) {
            return doubleCompare(value, compareValue, symbol.replace("double", ""));
        } else if (symbol.contains("date")) {
            return dateCompare(value, compareValue, symbol.replace("date", ""));
        }
        return false;
    }


    /**
     * 数值计算
     *
     * @param afterValue  数值
     * @param beforeValue 操作值
     * @param symbol      操作符号
     */
    public static Object operate(Object afterValue, Object beforeValue, String symbol) {
        if (symbol.equals("==")) {
            return beforeValue;
        } else {
            //String类型
            if (afterValue.getClass().isAssignableFrom(String.class)) {
                return stringOperate(afterValue, beforeValue, symbol);
            }
            //Integer类型
            else if (afterValue.getClass().isAssignableFrom(Integer.class)) {
                return intOperate(afterValue, beforeValue, symbol);
            }
            //Double类型
            else if (afterValue.getClass().isAssignableFrom(Double.class)) {
                return doubleOperate(afterValue, beforeValue, symbol);
            }
            //Float类型
            else if (afterValue.getClass().isAssignableFrom(Float.class)) {
                return floatOperate(afterValue, beforeValue, symbol);
            }
            //LocalDateTime类型
            else if (afterValue.getClass().isAssignableFrom(LocalDateTime.class)) {
                return dateOperate(afterValue, beforeValue, symbol);
            }
        }

        return null;
    }

    /**
     * double类型数值计算
     *
     * @param afterValue  数值
     * @param beforeValue 计算值
     * @param symbol      计算符合
     */
    public static double doubleOperate(Object afterValue, Object beforeValue, String symbol) {
        double value1 = Double.parseDouble(afterValue.toString());
        double value2 = Double.parseDouble(beforeValue.toString());
        switch (symbol) {
            case "+":
                return value1 + value2;
            case "-":
                return value1 - value2;
            case "*":
                return value1 * value2;
            case "/":
                return value1 / value2;
            default:
                break;
        }
        return value2;
    }

    /**
     * float类型数值计算
     *
     * @param afterValue  数值
     * @param beforeValue 计算值
     * @param symbol      计算符合
     */
    public static float floatOperate(Object afterValue, Object beforeValue, String symbol) {
        float value1 = Float.parseFloat(afterValue.toString());
        float value2 = Float.parseFloat(beforeValue.toString());
        switch (symbol) {
            case "+":
                return value1 + value2;
            case "-":
                return value1 - value2;
            case "*":
                return value1 * value2;
            case "/":
                return value1 / value2;
            default:
                break;
        }
        return value2;
    }

    /**
     * int类型数值计算
     *
     * @param afterValue  数值
     * @param beforeValue 计算值
     * @param symbol      计算符合
     */
    public static int intOperate(Object afterValue, Object beforeValue, String symbol) {
        int value1 = Integer.parseInt(afterValue.toString());
        int value2 = Integer.parseInt(beforeValue.toString());
        switch (symbol) {
            case "+":
                return value1 + value2;
            case "-":
                return value1 - value2;
            case "*":
                return value1 * value2;
            case "/":
                return value1 / value2;
            default:
                break;
        }
        return value2;
    }

    /**
     * LocalDateTime类型数值计算
     *
     * @param afterValue  数值
     * @param beforeValue 计算值
     * @param symbol      计算符合
     */
    public static LocalDateTime dateOperate(Object afterValue, Object beforeValue, String symbol) {
        //数据格式为: 数据_单位
        //单位：year(年),month(月),day(日),hour(时),minute(分),second(秒)
        String[] array = beforeValue.toString().split("_");
        LocalDateTime value1 = DateUtils.strToDate(afterValue.toString());
        LocalDateTime value2 = DateUtils.operate(value1, Integer.parseInt(array[0]), array[1], symbol);
        if (value2 != null) {
            return value2;
        }
        return value1;
    }

    /**
     * string类型数值计算
     *
     * @param afterValue  数值
     * @param beforeValue 计算值
     * @param symbol      计算符合
     */
    public static String stringOperate(Object afterValue, Object beforeValue, String symbol) {
        String value1 = afterValue.toString();
        String value2 = beforeValue.toString();
        if ("+".equals(symbol)) {
            return value1 + value2;
        } else if ("-".equals(symbol)) {
            return value1.replace(value2, "");
        } else if (symbol.contains("int")) {
            return intOperate(afterValue, beforeValue, symbol.replace("int", "")) + "";
        } else if (symbol.contains("float")) {
            return floatOperate(afterValue, beforeValue, symbol.replace("float", "")) + "";
        } else if (symbol.contains("double")) {
            return doubleOperate(afterValue, beforeValue, symbol.replace("double", "")) + "";
        } else if (symbol.contains("date")) {
            return dateOperate(afterValue, beforeValue, symbol.replace("date", "")) + "";
        }
        return value1;
    }

}
