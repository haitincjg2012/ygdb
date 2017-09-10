package com.apec.framework.common.exception;

/**
 * 类 编 号：BL_PU6030202_SearchServerException
 * 类 名 称：SearchServerException
 * 内容摘要：搜索后台服务异常
 * 完成日期：2016-07-23
 * 编码作者：
 */
public class SearchServerException extends ServerException
{
    private static final long serialVersionUID = 1016453527296702100L;

    public SearchServerException(String errorCode, String message)
    {
        super(errorCode, message);
    }

    public SearchServerException(String errorCode, Throwable ex)
    {
        super(errorCode, ex);
    }

    public SearchServerException(String errorCode, String message, Throwable e)
    {
        super(errorCode, message, e);
    }

    public String toString()
    {
        return super.toString();
    }
}
