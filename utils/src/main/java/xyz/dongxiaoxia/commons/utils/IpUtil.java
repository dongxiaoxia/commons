package xyz.dongxiaoxia.commons.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dongxiaoxia on 2017/6/22.
 * IP地址工具类
 */
public class IpUtil {

    private static  final String UNKNOWN = "unknown";

    /**
     * 根据 HttpServletRequest 获取真实IP地址
     *
     * @param request 请求
     * @return 真实IP地址
     */
    public static String getRealIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getRealIpV2(HttpServletRequest request) {
        String accessIP = request.getHeader("x-forwarded-for");
        if (null == accessIP)
            return request.getRemoteAddr();
        return accessIP;
    }

}
