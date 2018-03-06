package com.apec.framework.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Title: 验证工具
 *
 * @author yirde  2017/7/21.
 */
public class ValidateUtil {

    /**
     * 手机号校验
     */
    private static final Pattern MOBILE_VALIDATE_FORMART = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");

    /**
     * //18位身份证验证正则
     */
    private static final Pattern INMBER_VALIDATE_FORMART_18 = Pattern.compile("^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$");

    /**
     * //15位身份证验证正则
     */
    private static final Pattern INMBER_VALIDATE_FORMART_15 = Pattern.compile("^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$");

    /**
     * 18位身份证号的长度
     */
    private static final int INMBER_VALIDATE_EIGHTY_LENGTH = 18;

    /**
     * 15位身份证号的长度
     */
    private static final int INMBER_VALIDATE_FIVTY_LENGTH = 15;

    /**
     * 验证手机号码是否正确
     * 13 14 15 17 18
     * @param mobile Mobile
     * @return true 正确 false 错误
     */
    public static boolean checkMobile(String mobile) {
        if(StringUtils.isBlank(mobile)) {
            return false;
        }
        // 验证手机号
        Matcher m = MOBILE_VALIDATE_FORMART.matcher(mobile);
        return  m.matches();
    }

    /**
     * 校验身份证
     * @param idNumber idNumber
     * @return boolean
     */
    public static boolean checkIdNumber(String idNumber) {
        if(StringUtils.isBlank(idNumber)) {
            return false;
        }
        if(idNumber.length() != INMBER_VALIDATE_EIGHTY_LENGTH && idNumber.length() != INMBER_VALIDATE_FIVTY_LENGTH){
            //身份证号码不是15位（一代身份证）或18位，则说明身份证号码格式有误
            return false;
        }
        // 身份证校验正则
        Pattern  p = INMBER_VALIDATE_FORMART_18;
        if(idNumber.length() == INMBER_VALIDATE_FIVTY_LENGTH){
            //15位身份证对应正则
            p = INMBER_VALIDATE_FORMART_15;
        }
        Matcher m = p.matcher(idNumber);
        return  m.matches();
    }

}
