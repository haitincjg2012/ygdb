/**
 * 版权所有：版权所有(C) 2016，中农网 
 * 文件编号：BL_PU6030301_ResultData
 * 文件名称：ResultData.java 
 * 系统编号：
 * 系统名称：
 * 组件编号：
 * 组件名称：
 * 设计作者：
 * 完成日期：2016-07-14
 * 设计文档：
 * 内容摘要：json服务后台返回实体
 */
package com.apec.framework.common;

import java.io.Serializable;

/**
 * 类 编 号：BL_PU6030301_ResultData
 * 类 名 称：ResultData
 * 内容摘要：json服务返回实体
 * 完成日期：2016-07-14
 * 编码作者：
 */
public class ResultData<T> implements Serializable
{
    private static final long serialVersionUID = 8450400755456555766L;

    private boolean succeed = true;// 结果: 成功或失败(true,false)

    private String errorCode = "";// 错误码

    private String errorMsg = "";// 错误信息

    private T data = null;// 业务数据实体

    private String repeatAct; //返回前端生成的防止重复提交唯一值

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
     * @return
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
     * @return
     */
    public boolean isSucceed()
    {
        return succeed;
    }

    /**
     * 设置业务处理结果:成功或失败
     * @return
     */
    public void setSucceed(boolean succeed)
    {
        this.succeed = succeed;
    }

    /**
     * 获取业务数据实体
     * @return
     */
    public T getData()
    {
        return data;
    }

    /**
     * 设置业务数据实体
     * @return
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
