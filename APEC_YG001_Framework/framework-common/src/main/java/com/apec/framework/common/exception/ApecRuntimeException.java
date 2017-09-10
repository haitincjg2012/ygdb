/**
 * 版权所有：版权所有(C) 2016，中农网
 * 文件编号：BL_PU6030202_ApecRuntimeException.java
 * 文件名称：ApecRuntimeException.java
 * 系统编号：
 * 系统名称：
 * 组件编号：
 * 组件名称：
 * 设计作者：
 * 完成日期：2016-07-25
 * 设计文档：
 * 内容摘要：运行期异常
 */
package com.apec.framework.common.exception;

import java.util.Arrays;

/**
 * 类 编 号：BL_PU6030202_ApecRuntimeException
 * 类 名 称：ApecRuntimeException
 * 内容摘要：运行期异常
 * 完成日期：2016-07-25
 * 编码作者：
 */
public class ApecRuntimeException extends RuntimeException
{
    private static final long serialVersionUID = -7976146395762011863L;

    private String message;

    private String errorCode = null;

    /**
     * 动态参数
     */
    private final Object[] args;

    public Object[] getArgs()
    {
        return Arrays.copyOf( args, args.length );
    }

    public String getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }

    public ApecRuntimeException(Object... args)
    {
        super();
        this.args = args;
    }

    public ApecRuntimeException(String errorCode, Object... args)
    {
        this.errorCode = errorCode;
        this.args = args;
    }

    public ApecRuntimeException(String errorCode, String message, Object... args)
    {
        super( message );
        this.errorCode = errorCode;
        this.message = message;
        this.args = args;
    }

    public ApecRuntimeException(String errorCode, Throwable ex, Object... args)
    {
        super( ex );
        this.errorCode = errorCode;
        this.args = args;
    }

    public ApecRuntimeException(String errorCode, String message, Throwable e, Object... args)
    {
        super( message, e );
        this.errorCode = errorCode;
        this.message = message;
        this.args = args;
    }

    public String getMessage()
    {
        return this.message;
    }

    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append( super.toString() );
        if(null != message)
        {
            buf.append( "[" + message + "]" );
        }
        if(null != errorCode)
        {
            buf.append( "<" + errorCode + ">" );
        }
        return buf.toString();
    }
}
