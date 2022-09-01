package com.zny.common.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author WBS
 * Date:2022/9/1
 */

public class IpUtils {
    /**
     * 获取真实ip
     *
     * @param request HttpServletRequest
     * @return 真实ip
     */
    public static String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isNotBlank(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (isIpValid(ip)) {
            return ip;
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (isIpValid(ip)) {
            return ip;
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isIpValid(ip)) {
            return ip;
        }
        ip = request.getHeader("HTTP_CLIENT_IP");
        if (isIpValid(ip)) {
            return ip;
        }
        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (isIpValid(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Real-IP");
        if (isIpValid(ip)) {
            return ip;
        }
        ip = request.getRemoteAddr();
        return ip;
    }

    /**
     * 仅仅判断ip是否有效
     *
     * @param ip
     * @return
     */
    private static boolean isIpValid(String ip) {
        if (StringUtils.isBlank(ip)) {
            return false;
        }
        String[] split = ip.split("\\.");
        if (split.length != 4) {
            return false;
        }
        try {
            long first = Long.valueOf(split[0]);
            long second = Long.valueOf(split[1]);
            long third = Long.valueOf(split[2]);
            long fourth = Long.valueOf(split[3]);
            return first < 256 && first > 0 && second < 256 && second >= 0 && third < 256 && third >= 0 && fourth < 256 && fourth >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
