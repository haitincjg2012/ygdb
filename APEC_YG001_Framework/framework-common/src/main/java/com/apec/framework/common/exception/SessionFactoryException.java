package com.apec.framework.common.exception;

/**
 * 类 编 号：BL_PU6030202_SessionFactoryException
 * 类 名 称：SessionFactoryException
 * 内容摘要：SessionFactory异常
 * 完成日期：2016-07-23
 * 编码作者：
 */
public class SessionFactoryException extends ApecRuntimeException
{
    private static final long serialVersionUID = 2765681008872743746L;

    public SessionFactoryException(String errorCode, String message)
    {
        super(errorCode, message);
    }

    public SessionFactoryException(String errorCode, Throwable ex)
    {
        super(errorCode, ex);
    }

    public SessionFactoryException(String errorCode, String message, Throwable e)
    {
        super(errorCode, message, e);
    }

    public String toString()
    {
        return super.toString();
    }
}
