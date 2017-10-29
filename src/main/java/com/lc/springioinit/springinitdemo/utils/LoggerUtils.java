package com.lc.springioinit.springinitdemo.utils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;

/**
 * @auther lichi
 * @create 2017-10-29 20:51
 */
public class LoggerUtils {

    public static String LOGGER_RETURN = "responseData";

    /**
     * 获取Request请求IP地址
     * @param request
     * @return
     * @throws Exception
     */
    public static String getClientIp(HttpServletRequest request) throws Exception {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null) {
            if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
                int index = ip.indexOf(",");
                if (index != -1) {
                    return ip.substring(0, index);
                } else {
                    return ip;
                }
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (ip != null) {
            if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (ip != null) {
            if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip != null) {
            if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        ip = request.getRemoteAddr();
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }

    /**
     * 获取HttpServletRequest的Request Type
     * @param request
     * @return
     * @throws Exception
     */
    public static String getRequestType(HttpServletRequest request) throws Exception {
        String requestType = request.getHeader("X-Requested-With");

        if (requestType != null && requestType == "XMLHttpRequest")
            return "Ajax";
        else
            return "Normal";
    }

    /**
     * 将毫秒数转换为“HH:mm:ss”格式
     * @param mills
     * @return
     */
    public static String mills2Time(Long mills) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        return simpleDateFormat.format(mills);
    }

}
