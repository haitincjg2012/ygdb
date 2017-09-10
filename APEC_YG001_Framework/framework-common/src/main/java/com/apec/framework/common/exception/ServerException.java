package com.apec.framework.common.exception;

/**
 * 类 编 号：BL_PU6030202_ServerException
 * 类 名 称：ServerException
 * 内容摘要：后台服务器异常
 * 完成日期：2016-07-23
 * 编码作者：
 */
public class ServerException extends ApecRuntimeException
{
    private static final long serialVersionUID = -5650322399598065544L;

    public ServerException(String errorCode, String message)
    {
        super(errorCode, message);
    }

    public ServerException(String errorCode, Throwable ex)
    {
        super(errorCode, ex);
    }

    public ServerException(String errorCode, String message, Throwable e)
    {
        super(errorCode, message, e);
    }

    public String toString()
    {
        return super.toString();
    }
}
