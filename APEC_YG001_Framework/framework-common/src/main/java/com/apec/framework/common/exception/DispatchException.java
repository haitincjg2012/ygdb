package com.apec.framework.common.exception;

/**
 * 类 编 号： BL_PU4320201_DispatchException
 * 类 名 称： DispatchException
 * 内容摘要:http请求分发异常
 * 完成日期： 2016-07-25
 * 编码作者： 
*/
public class DispatchException extends ApecRuntimeException
{

    private static final long serialVersionUID = 1336373165807040572L;

    public DispatchException(String errorCode, String message)
    {
        super(errorCode, message);
    }

    public DispatchException(String errorCode, Throwable ex)
    {
        super(errorCode, ex);
    }

    public DispatchException(String errorCode, String message, Throwable e)
    {
        super(errorCode, message, e);
    }

    public String toString()
    {
        return super.toString();
    }

}
