package com.apec.framework.common.exception;

/**
 * 类 编 号：BL_PU6030202_IndexServerException
 * 类 名 称：IndexServerException
 * 内容摘要：后台索引服务异常
 * 完成日期：2016-07-23
 * 编码作者：
 */
public class IndexServerException extends ServerException
{

    private static final long serialVersionUID = 7055129015035503591L;

    public IndexServerException(String errorCode, String message)
    {
        super(errorCode, message);
    }

    public IndexServerException(String errorCode, Throwable ex)
    {
        super(errorCode, ex);
    }

    public IndexServerException(String errorCode, String message, Throwable e)
    {
        super(errorCode, message, e);
    }

    public String toString()
    {
        return super.toString();
    }
}
