package com.apec.framework.common.exception;

/**
 * 类 编 号： BL_PU4320201_DataSourceException
 * 类 名 称： DataSourceException
 * 内容摘要: 数据源异常
 * 完成日期： 2016-07-25
 * 编码作者： 
*/
public class DataSourceException extends ApecRuntimeException
{

    private static final long serialVersionUID = -6772900713940381707L;

    public DataSourceException(String errorCode, String message)
    {
        super(errorCode, message);
    }

    public DataSourceException(String errorCode, Throwable ex)
    {
        super(errorCode, ex);
    }

    public DataSourceException(String errorCode, String message, Throwable e)
    {
        super(errorCode, message, e);
    }

    public String toString()
    {
        return super.toString();
    }
}
