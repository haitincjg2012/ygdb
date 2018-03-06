package com.apec.framework.common.exception;

/**
 * @author xx
 */
public class DomainServiceException extends ApecRuntimeException
{
    private static final long serialVersionUID = -240536959932968350L;

    public DomainServiceException(String errorCode, String message)
    {
        super(errorCode, message);
    }

    public DomainServiceException(String errorCode, Throwable ex)
    {
        super(errorCode, ex);
    }

    public DomainServiceException(String errorCode, String message, Throwable e)
    {
        super(errorCode, message, e);
    }

    @Override
    public String toString()
    {
        return super.toString();
    }
}
