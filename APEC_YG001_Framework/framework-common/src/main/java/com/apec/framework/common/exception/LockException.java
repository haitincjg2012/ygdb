package com.apec.framework.common.exception;

/**
 * 类 名 称：LockException
 * 内容摘要：LOCK服务异常
 * 完成日期：2016-07-23
 * 编码作者：
 */
public class LockException extends ApecRuntimeException
{

    public LockException(String errorCode, String message)
    {
        super(errorCode, message);
    }

    public LockException(String errorCode, Throwable ex)
    {
        super(errorCode, ex);
    }

    public LockException(String errorCode, String message, Throwable e)
    {
        super(errorCode, message, e);
    }

    public String toString()
    {
        return super.toString();
    }
}
