package com.apec.framework.common.exception;

/**
 *
 * @Description:业务异常处理
 *
 * @author huangcl
 *
 * @date 2016年8月17日
 *
 */
public class BusinessException extends ApecRuntimeException
{
    private static final long serialVersionUID = 1498884168645406117L;

    public BusinessException(String errorCode, Object... args)
    {
        super( errorCode, args );
    }

    public BusinessException(String errorCode, String message, Object... arguments)
    {
        super( errorCode, message, arguments );
    }

    public BusinessException(String errorCode, Throwable ex, Object... arguments)
    {
        super( errorCode, ex, arguments );
    }

    public BusinessException(String errorCode, String message, Throwable e, Object... arguments)
    {
        super( errorCode, message, e, arguments );
    }

    public String toString()
    {
        return super.toString();
    }
}
