package com.apec.framework.common.util;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类 编 号：BL_PU9200202_StringUtil
 * 类 名 称：BaseStringUtil
 * 内容摘要：字符串工具类
 * 完成日期：2016-07-25
 * 编码作者：
 * @author xx
 */
public abstract class BaseStringUtil
{
    private static final Pattern INT_PATTERN = Pattern.compile("^-?\\d+$");

    private static final Pattern INT_PATTERN_STR = Pattern.compile("\\s*|\t|\r|\n");

    /**
     * 获取32位uuid
     * @return String
     */
    public synchronized static String getUUID()
    {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 获取36位uuid
     * @return String
     */
    public synchronized static String get36UUID()
    {
        return UUID.randomUUID().toString();
    }

    /**
     * 连接字符串
     * @param str str
     * @return String
     */
    public static boolean isInvalidStr(String str)
    {
        return null == str || (null != str && "undefined".equalsIgnoreCase(str));
    }

    /**
     * 是否为数字
     * @param  str str
     * @returnboolean
     */
    public static boolean isInteger(String str)
    {
        Matcher matcher = INT_PATTERN.matcher(str);
        return matcher.find();
    }

    /**
     * 字符串转换为数字
     * @param s s
     * @return int
     */
    public static int convertStringToInt(String s)
    {
        int param;
        if(isInteger(s))
        {
            param = Integer.parseInt(s);
        }
        else
        {
            throw new IllegalArgumentException("Argum occured exception in convert String to int");
        }
        return param;
    }

    /**
     * 字符串转换为数字
     * @param str str
     * @return  String
     */
    public static String getNotNullString(String str)
    {
        return str == null ? "" : str;
    }

    /**
     * 连接字符串
     * @param array array
     * @param splitStr splitStr
     * @return String
     */
    public static String getJoinString(Object[] array, String splitStr)
    {
        String result = null;
        if(null != array && array.length > 0)
        {
            StringBuffer buf = new StringBuffer();
            for(Object str : array)
            {
                buf.append(str).append(splitStr);
            }
            result = buf.toString();
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * 连接字符串
     * @param list list
     * @param splitStr splitStr
     * @return String
     */
    public static String getJoinString(List<? extends Serializable> list, String splitStr)
    {
        String result = null;
        if(null != list && list.size() > 0)
        {
            StringBuffer buf = new StringBuffer();
            for(Object str : list)
            {
                buf.append(str).append(splitStr);
            }
            result = buf.toString();
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * 字符串转换为数字
     * @param array array
     * @param splitStr splitStr
     * @return String
     */
    public static String getSqlJoinString(Object[] array, String splitStr)
    {
        String result = null;
        if(null != array && array.length > 0)
        {
            StringBuffer buf = new StringBuffer();
            for(Object str : array)
            {
                buf.append("'").append(str).append("'").append(splitStr);
            }
            result = buf.toString();
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * 字符串转换为数字
     * @param list list
     * @param splitStr splitStr
     * @return String
     */
    public static String getSqlJoinString(List<String> list, String splitStr)
    {
        String result = null;
        if(null != list && list.size() > 0)
        {
            StringBuffer buf = new StringBuffer();
            for(String str : list)
            {
                buf.append("'").append(str).append("'").append(splitStr);
            }
            result = buf.toString();
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * 根据可变字符串生成一个连接字符串 ,例如"1","2","3"可以连接成"1,2,3"或"1, 2, 3"
     * @param joinStr 字符串连接符号
     * @param isBlank 字符串后是否需要空格
     * @param strArray 需要连接的字符串可变参数
     * @return 连接字符串
     */
    public static String getJoinVarString(String joinStr, boolean isBlank, String... strArray)
    {
        String result = null;
        if(null != strArray && strArray.length > 0)
        {
            StringBuffer buf = new StringBuffer();
            for(String str : strArray)
            {
                if(isBlank)
                {
                    buf.append(str).append(" ").append(joinStr);
                }
                else
                {
                    buf.append(str).append(joinStr);
                }
            }
            result = buf.toString();
            if(!"".equals(joinStr))
            {
                result = result.substring(0, result.length() - 1);
            }
        }
        return result;
    }

    /**
     * 根据可变字符串生成一个连接字符串,例如"1","2","3"可以连接成"1,2,3"或"123"等
     * @param joinStr 字符串连接符号
     * @param strArray 需要连接的字符串可变参数
     * @return 连接字符串
     */
    public static String getJoinVarString(String joinStr, String... strArray)
    {
        return getJoinVarString(joinStr, false, strArray);
    }

    public static String replaceBlank(String str)
    {
        String dest = "";
        if(str != null)
        {
            Matcher m = INT_PATTERN_STR.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static String  hiddenMobile(String mobile){
        if(StringUtils.isNotBlank(mobile)) {
            return mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
        }
        return mobile;
    }
}
