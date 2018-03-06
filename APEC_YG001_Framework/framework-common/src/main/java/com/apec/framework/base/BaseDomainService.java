package com.apec.framework.base;

import com.alibaba.fastjson.TypeReference;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.tools.BaseIDGenerator;
import com.apec.framework.common.util.BaseJsonUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 类 编 号：BL_PU6030202_BaseDomainService
 * 类 名 称：BaseDomainService
 * 内容摘要：抽象基础DS,以后扩展使用
 * 完成日期：2016-07-23
 * @author 编码作者：
 */
public abstract class BaseDomainService
{
    /**
     * 生成实体表编号
     * @param prefix 前置
     * @return String
     */
    protected synchronized static String generateNO(String prefix)
    {
        return prefix + BaseIDGenerator.getWorkId();
    }

    /**
     * 生成实体表编号
     * @return String
     */
    protected synchronized static String generateID()
    {
        return String.valueOf( BaseIDGenerator.getWorkId());
    }

    /**
     * 这里的resultData T 的数据只能为对象
     * @param jsonStr 服务所返回的数据
     * @param clazz json所需的对象类型
     * @param <T> 声明对象属性
     * @return ResultData<T>
     */
    protected <T> ResultData<T> getResultJSON(String jsonStr, Class<T> clazz)
    {
        ResultData<T> resultDataJSON = BaseJsonUtil.parseObject(jsonStr, new TypeReference<ResultData<T>>()
        {
        });
        // 泛型类型调用parseObject的时候，使用parseObject可以转换Class，
        // 但是后边传TypeReference或者Type就解析不出泛型类型了，需要再转换一次
        if(null != resultDataJSON.getData() && StringUtils.isNotBlank(resultDataJSON.getData().toString()))
        {
            T data = BaseJsonUtil.parseObject(resultDataJSON.getData().toString(), clazz);
            resultDataJSON.setData(data);
        }
        return resultDataJSON;
    }

    /**
     * @param text 需要转换JSON数据
     * @param clazz JOSN所属类型
     * @return <T> T
     */
    protected <T> T parseObject(String text, Class<T> clazz)
    {
        return BaseJsonUtil.parseObject(text, clazz);
    }

    /**
     * 转化为集合对象
     * @param text 需要转换JSON数据
     * @param clazz JOSN所属类型
     * @return <T> List<T>
     */
    protected <T> List<T> parseArray(String text, Class<T> clazz)
    {
        return BaseJsonUtil.parseArray(text, clazz);
    }
}
