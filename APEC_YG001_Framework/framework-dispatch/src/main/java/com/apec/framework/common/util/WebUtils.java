package com.apec.framework.common.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * Title: WebUtils
 *
 * @author yirde  2017/7/12.
 */
public class WebUtils {

    private static final Logger log = LoggerFactory.getLogger( WebUtils.class );

    private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    private static final Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);

    /**
     * 检验输入的captcha是否与Session中的一致
     *
     * @param request
     * @param captcha
     * @return true代表一致
     */
//    public static boolean checkCaptcha(HttpServletRequest request, String captcha) {
//        Captcha c = (Captcha) request.getSession().getAttribute(Captcha.NAME);
//        //if (c != null && c.isCorrect(captcha)) {
//        if (c != null && c.getAnswer().toUpperCase().equals(captcha.toUpperCase())) {
//            request.getSession().removeAttribute(Captcha.NAME);
//            return true;
//        }
//        return false;
//    }

    /**
     * 获取IP地址
     *
     * @param request
     * @return
     */
    public static String getIP(HttpServletRequest request) {
        String IP = request.getRemoteAddr();
        String forwarded = request.getHeader("x-forwarded-for");
        if (forwarded != null) {
            forwarded = forwarded.split(",", 2)[0];
            if (pattern.matcher(forwarded).matches()) {
                return forwarded;
            }
        }
        if (pattern.matcher(IP).matches()) {
            return IP;
        } else {
            log.warn("IP is not valid.[IP={}]", IP);
            return "";
        }
    }
}
