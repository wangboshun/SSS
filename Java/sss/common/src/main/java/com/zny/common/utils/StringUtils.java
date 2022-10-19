package com.zny.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author WBS
 * Date:2022/9/7
 * 字符串帮助类
 */

public class StringUtils {
    private static final Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 下划线格式 -> 驼峰  大小写均可
     */
    public static String lowerLineToHump(String lowerLineAndUppercaseStr) {
        //拆分成数组
        String[] eachStr = lowerLineAndUppercaseStr.split("_");
        StringBuilder resStr = new StringBuilder();
        String firstStr = "";
        String tempStr = "";
        for (int i = 0; i < eachStr.length; i++) {
            //第一个数组全部小写
            if (i == 0) {
                firstStr = eachStr[0].toLowerCase();
                resStr.append(firstStr);
            } else {
                //以后的数组首字母大写
                tempStr = capitalizeTheFirstLetter(eachStr[i]);
                resStr.append(tempStr);
            }
        }

        return resStr.toString();
    }

    /**
     * 任意字符串 -> 首字母大写
     */
    public static String capitalizeTheFirstLetter(String str) {
        char firstChar = str.toUpperCase().charAt(0);
        String nextStr = str.toLowerCase().substring(1);
        return firstChar + nextStr;
    }

    /**
     * 驼峰 -> 下划线格式 默认小写,存在第二个形参且为true时大写.
     */
    public static String humpToLowerLine(String humpStr, boolean... defaultUppercaseAndTrueLowercase) {
        Matcher matcher = Pattern.compile("[A-Z]").matcher(humpStr);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);

        //如果第二个形参为true 转为大写
        if (defaultUppercaseAndTrueLowercase.length >= 1 && defaultUppercaseAndTrueLowercase[0]) {
            return sb.toString().toUpperCase();
        }
        return sb.toString();
    }
}
