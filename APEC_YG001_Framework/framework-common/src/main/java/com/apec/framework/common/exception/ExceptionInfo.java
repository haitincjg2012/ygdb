package com.apec.framework.common.exception;

import java.io.Serializable;

public class ExceptionInfo<T> implements Serializable
{
    private static final long serialVersionUID = 5841390047513892294L;
    
    public static final Integer OK = 0;

    public static final Integer ERROR = 100;

    private String errorCode;

    private String errorMessgae;

    private String url;
    
    private T data;


    public String getErrorMessgae()
    {
        return errorMessgae;
    }

    public void setErrorMessgae(String errorMessgae)
    {
        this.errorMessgae = errorMessgae;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public String getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }

}
