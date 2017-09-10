/**
 * 版权所有：版权所有(C) 2016，中农网
 * 文件编号：BL_PU6030202_StringUtil.java
 * 文件名称：StringUtil.java
 * 系统编号：Z0009014
 * 系统名称：
 * 组件编号：
 * 组件名称：
 * 设计作者：
 * 完成日期：
 * 设计文档：
 * 内容摘要：字符串工具类
 */
package com.apec.framework.common.util;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类 编 号：BL_PU9200202_StringUtil
 * 类 名 称：StringUtil
 * 内容摘要：字符串工具类
 * 完成日期：2016-07-25
 * 编码作者：
 */
public abstract class StringUtil
{
    private static final Pattern INT_PATTERN = Pattern.compile("^-?\\d+$");

    /**
     * 获取32位uuid
     * @return
     */
    public synchronized static String getUUID()
    {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 获取36位uuid
     * @return
     */
    public synchronized static String get36UUID()
    {
        return UUID.randomUUID().toString();
    }

    /**
     * 连接字符串
     * @param str
     * @return String
     */
    public static boolean isInvalidStr(String str)
    {
        return null == str || (null != str && "undefined".equalsIgnoreCase(str));
    }

    /**
     * 是否为数字
     * @param str
     * @return
     */
    public static boolean isInteger(String str)
    {
        Matcher matcher = INT_PATTERN.matcher(str);
        return matcher.find();
    }

    /**
     * 字符串转换为数字
     * @param s
     * @return
     * @throws ArgumentException
     */
    public static int convertStringToInt(String s)
    {
        int param = 0;
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
     * @param s
     * @return
     * @throws ArgumentException
     */
    public static String getNotNullString(String str)
    {
        return str == null ? "" : str;
    }

    /**
     * 连接字符串
     * @param array
     * @param splitStr
     * @return
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
     * @param list
     * @param splitStr
     * @return
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
     * @param array
     * @param splitStr
     * @return
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
     * @param list
     * @param splitStr
     * @return
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
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
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
