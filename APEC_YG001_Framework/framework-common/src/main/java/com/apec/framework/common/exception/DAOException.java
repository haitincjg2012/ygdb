package com.apec.framework.common.exception;
/**
 * 类 编 号： BL_PU4320201_DAOException
 * 类 名 称： DAOException
 * 内容摘要: DAO异常
 * 完成日期： 2016-07-25
 * 编码作者： 
*/
public class DAOException extends ApecRuntimeException
{
    private static final long serialVersionUID = -86574251951571653L;

    public DAOException(String errorCode, String message)
    {
        super(errorCode, message);
    }

    public DAOException(String errorCode, Throwable ex)
    {
        super(errorCode, ex);
    }

    public DAOException(String errorCode, String message, Throwable e)
    {
        super(errorCode, message, e);
    }

    public String toString()
    {
        return super.toString();
    }
}
