package com.apec.framework.common.exception;

/**
 * 类 名 称：LockExistsException
 * 内容摘要：LOCKEXIST服务异常
 * 完成日期：2016-07-23
 * 编码作者：
 */
public class LockExistsException extends ApecRuntimeException
{

    public LockExistsException(String errorCode, String message)
    {
        super(errorCode, message);
    }

    public LockExistsException(String errorCode, Throwable ex)
    {
        super(errorCode, ex);
    }

    public LockExistsException(String errorCode, String message, Throwable e)
    {
        super(errorCode, message, e);
    }

    public String toString()
    {
        return super.toString();
    }
}
