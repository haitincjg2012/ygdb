package com.apec.framework.common;

import java.io.Serializable;
import java.util.Map;

/**
 * 类 编 号：
 * 类 名 称：PageJSON
 * 内容摘要：封装页面过来的JSON
 * 完成日期：
 * 编码作者：
 */
public class PageJSON<T> implements Serializable
{
    private static final long serialVersionUID = 7672548110652952249L;

    private T formJSON = null;

    private Map<String, String> requestParameterMap;// 请求参数

    private Map<String, Object> requestAttrMap;// 请求属性

    public Map<String, String> getRequestParameterMap()
    {
        return requestParameterMap;
    }

    public void setRequestParameterMap(Map<String, String> requestParameterMap)
    {
        this.requestParameterMap = requestParameterMap;
    }

    public Map<String, Object> getRequestAttrMap()
    {
        return requestAttrMap;
    }

    public void setRequestAttrMap(Map<String, Object> requestAttrMap)
    {
        this.requestAttrMap = requestAttrMap;
    }

    public T getFormJSON()
    {
        return formJSON;
    }

    public void setFormJSON(T formJSON)
    {
        this.formJSON = formJSON;
    }

}
