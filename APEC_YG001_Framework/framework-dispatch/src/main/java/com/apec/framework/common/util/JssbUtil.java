package com.apec.framework.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 框架工具类,与任何业务、模块无关
 * @author Administrator
 *
 */
public abstract class JssbUtil
{

    /**
     * 校验Map中是否包含key
     * @param ht
     * @param key
     * @return
     */
    public static boolean isContainKey(Map<?, ?> ht, String key)
    {
        boolean flag = false;
        if(null != key && null != ht && ht.containsKey(key))
        {
            flag = true;
        }
        return flag;
    }

    /**
     * 命令是否合法
     * @param commandName
     * @return
     */
    public static boolean illegalCommand(String commandName)
    {
        return "".equals(commandName) || "null".equals(commandName);
    }

    /**
     * 组装请求json
     * @param requestJson
     * @param reqParameterJson
     * @param reqAttJson
     * @return
     */
    public static synchronized String assemblRequestJSON(String requestJson, String reqParameterJson, String reqAttJson)
    {
        StringBuffer buf = new StringBuffer();
        buf.append("{\"formJSON\":");
        buf.append(requestJson);
        buf.append(",");
        buf.append("\"requestParameterMap\":");
        buf.append(reqParameterJson);
        buf.append(",");
        buf.append("\"requestAttrMap\":");
        buf.append(reqAttJson);
        buf.append("}");
        return buf.toString();
    }

    /**
     * 拼接json
     * @param filedName 字段名称
     * @param value 字段值
     * @return
     */
    private static String assemblyJsonStr(String filedName, String value)
    {
        StringBuffer buf = new StringBuffer();
        if(StringUtils.isNotBlank(value))
        {
            buf.append("\"").append(filedName).append("\":\"").append(value).append("\"").append(",");
        }
        return buf.toString();
    }

}
