package com.apec.framework.common.exception;

/**
 * 类 编 号： BL_PU4320201_DataTypeException
 * 类 名 称： DataTypeException
 * 内容摘要: 数据类型异常
 * 完成日期： 2016-07-25
 * 编码作者： 
*/
public class DataTypeException extends ApecRuntimeException
{

    private static final long serialVersionUID = 1336373165807040572L;

    public DataTypeException(String errorCode, String message)
    {
        super(errorCode, message);
    }

    public DataTypeException(String errorCode, Throwable ex)
    {
        super(errorCode, ex);
    }

    public DataTypeException(String errorCode, String message, Throwable e)
    {
        super(errorCode, message, e);
    }

    public String toString()
    {
        return super.toString();
    }

}
