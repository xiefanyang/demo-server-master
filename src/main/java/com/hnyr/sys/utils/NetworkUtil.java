package com.hnyr.sys.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class NetworkUtil {
    private static String UNKNOWN = "unknown";

    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request
     * @return
     * @throws IOException
     */
    @SneakyThrows
    public static final String getIpAddress(HttpServletRequest request) {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址

        String ip = request.getHeader("X-Forwarded-For");
//        log.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);

        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
                log.debug("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
                log.debug("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
                log.debug("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                log.debug("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                log.debug("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!(UNKNOWN.equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }
}
