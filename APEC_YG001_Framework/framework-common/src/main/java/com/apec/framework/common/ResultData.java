package com.apec.framework.common;

import java.io.Serializable;

/**
 * 类 编 号：BL_PU6030301_ResultData
 * 类 名 称：ResultData
 * 内容摘要：json服务返回实体
 * 完成日期：2016-07-14
 * 编码作者：
 * @author xxx
 */
public class ResultData<T> implements Serializable
{
    private static final long serialVersionUID = 8450400755456555766L;

    /**
     * // 结果: 成功或失败(true,false)
     */
    private boolean succeed = true;

    /**
     * // 错误码
     */
    private String errorCode = "";

    /**
     * // 错误信息
     */
    private String errorMsg = "";

    /**
     * // 业务数据实体
     */
    private T data = null;

    /**
     * //返回前端生成的防止重复提交唯一值
     */
    private String repeatAct;

    public String getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }

    /**
     * 获取错误信息
     * @return errorMsg 错误信息
     */
    public String getErrorMsg()
    {
        return errorMsg;
    }

    /**
     * 设置错误信息
     * @param errorMsg 错误信息
     */
    public void setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }

    /**
     * 获取业务处理结果:成功或失败
     * @return boolean
     */
    public boolean isSucceed()
    {
        return succeed;
    }

    /**
     * 设置业务处理结果:成功或失败
     */
    public void setSucceed(boolean succeed)
    {
        this.succeed = succeed;
    }

    /**
     * 获取业务数据实体
     * @return T
     */
    public T getData()
    {
        return data;
    }

    /**
     * 设置业务数据实体
     */
    public void setData(T data)
    {
        this.data = data;
    }

    public String getRepeatAct()
    {
        return this.repeatAct;
    }

    public void setRepeatAct(String repeatAct)
    {
        this.repeatAct = repeatAct;
    }
}
