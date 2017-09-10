package com.apec.framework.common.exception;

/**
 * 类 编 号：BL_PU6030202_RestServiceException
 * 类 名 称：RestServiceException
 * 内容摘要：REST服务异常
 * 完成日期：2016-07-23
 * 编码作者：
 */
public class RestServiceException extends ApecRuntimeException
{
    private static final long serialVersionUID = 2954090457560199267L;

    public RestServiceException(String errorCode, String message)
    {
        super(errorCode, message);
    }

    public RestServiceException(String errorCode, Throwable ex)
    {
        super(errorCode, ex);
    }

    public RestServiceException(String errorCode, String message, Throwable e)
    {
        super(errorCode, message, e);
    }

    public String toString()
    {
        return super.toString();
    }
}
