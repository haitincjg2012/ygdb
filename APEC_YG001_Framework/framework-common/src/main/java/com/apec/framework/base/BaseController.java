package com.apec.framework.base;

import com.alibaba.fastjson.TypeReference;
import com.apec.framework.common.Constants;
import com.apec.framework.common.PageJSON;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.enumtype.Platform;
import com.apec.framework.common.util.JsonUtil;
import com.apec.framework.common.util.SpringUtil;
import com.apec.framework.dto.BaseDTO;
import com.apec.framework.dto.UserInfoVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

/**
 * 所有服务controller须继承该抽象类
 *
 * @author zhaolei
 * @create 2016-09-08 19:41
 */

public abstract class BaseController
{
    public static int pageNumber = 1;

    public static int pageSize = 10;

    /**
     * @param jsonStr 页面JSON字符串
     * @param clazz json所属类型
     * @return 根据泛型返回所需结果
     */
    protected <T> PageJSON<T> getPageJSON(String jsonStr, Class<T> clazz)
    {

        PageJSON<T> pageJSON = JsonUtil.parseObject( jsonStr, new TypeReference<PageJSON<T>>()
        {
        } );
        // 泛型类型调用parseObject的时候，使用parseObject可以转换Class，
        // 但是后边传TypeReference或者Type就解析不出泛型类型了，需要再转换一次
        if(null != pageJSON.getFormJSON() && StringUtils.isNotBlank( pageJSON.getFormJSON().toString() ))
        {
            T data = JsonUtil.parseObject( pageJSON.getFormJSON().toString(), clazz );
            pageJSON.setFormJSON( data );
        }
        return pageJSON;
    }

    /**
     * @param jsonStr 页面JSON字符串
     * @param clazz json所属类型
     * @return 根据泛型返回所需结果
     */
    protected <T> PageJSON<List<T>> getPageJSONList(String jsonStr, Class clazz)
    { 
        PageJSON<List<T>> pageJSON = JsonUtil.parseObject( jsonStr, new TypeReference<PageJSON<List<T>>>()
        {
        } );
        // 泛型类型调用parseObject的时候，使用parseObject可以转换Class，
        // 但是后边传TypeReference或者Type就解析不出泛型类型了，需要再转换一次
        if(null != pageJSON.getFormJSON() && StringUtils.isNotBlank( pageJSON.getFormJSON().toString() ))
        {
            List<T> data = JsonUtil.parseArray( pageJSON.getFormJSON().toString(), clazz );
            pageJSON.setFormJSON( data );
        }
        return pageJSON;
    }

    /**
     * @param text 需要转换JSON数据
     * @param clazz JOSN所属类型
     * @return 泛型对象
     */
    protected <T> T parseObject(String text, Class<T> clazz)
    {
        return JsonUtil.parseObject( text, clazz );
    }

    /**
     * JSON
     * @param obj Java对象
     * @return 页面JSON的Java对象
     */
    protected String toJSONString(Object obj)
    {
        return JsonUtil.toJSONString( obj );
    }

    /**
     * 生成返回json字符串
     * @param succeed 是否成功
     * @param errorCode 错误码
     * @param data 数据
     * @return JSON字符串
     */
    protected <T> String getResultJSONStr(boolean succeed, T data, String errorCode)
    {
        ResultData<T> ret = getResultData( succeed, data, errorCode );
        ret.setErrorMsg( SpringUtil.getMessage(errorCode));
        String retJSONStr = JsonUtil.toJSONString(ret);
        ret = null;
        return retJSONStr;
    }

    /**
     * 生成返回json字符串
     * @param succeed 是否成功
     * @param errorCode 错误码
     * @param data 数据
     * @return JSON字符串
     */
    protected <T> String getResultJSONStr(boolean succeed, T data, String errorCode, Object... args)
    {
        ResultData<T> ret = getResultData( succeed, data, errorCode, args );
        String retJSONStr = JsonUtil.toJSONString( ret );
        ret = null;
        return retJSONStr;
    }

    /**
     * 返回ResultData
     * @param <T> 泛型对象
     * @param succeed 是否成功
     * @param errorCode 错误码
     * @param data 数据对象
     * @param args 读取额外参数
     * @return 返回统一的数据结构
     */
    protected <T> ResultData<T> getResultData(boolean succeed, T data, String errorCode, Object... args)
    {
        ResultData<T> ret = new ResultData<>();
        ret.setSucceed( succeed );
        ret.setErrorCode( errorCode );
        if(StringUtils.isNotBlank( errorCode ))
        {
            ret.setErrorMsg( SpringUtil.getMessage( errorCode, args ) );
        }
        ret.setData( data );
        return ret;
    }

    /**
     * 获取终端类型
     * @param pageJSON 页面pageJSON数据
     * @return 终端类型
     */
    protected Platform getPlatform(PageJSON<?> pageJSON)
    {
        Map<String, Object> attrMap = pageJSON.getRequestAttrMap();
        String _c = "";
        if(null != attrMap && null != attrMap.get( Constants.CLIENT_TYPE_PARAM ))
        {
            _c = String.valueOf( attrMap.get( Constants.CLIENT_TYPE_PARAM ) );
        }
        Platform platform = Platform.getPlatform( _c.toUpperCase() );
        return platform;
    }

    /**
     * 获取用户帐号信息
     * @param pageJSON 页面JSON字符串
     * @return 返回用户编号
     */
    protected String getUserNo(PageJSON<?> pageJSON)
    {
        String userNo = null;
        if(null != pageJSON && null != pageJSON.getRequestAttrMap())
        {
            userNo = (String)pageJSON.getRequestAttrMap().get( Constants.USER_NO );
        }
        return userNo;
    }

    protected String getOrgCode(PageJSON<?> pageJSON)
    {
        String userNo = null;
        if(null != pageJSON && null != pageJSON.getRequestAttrMap())
        {
            userNo = (String)pageJSON.getRequestAttrMap().get( Constants.ORG_CODE );
        }
        return userNo;
    }

    /**
     * 获取用户信息
     * @param pageJSON 页面JSON字符串
     * @return 返回用户编号
     */
    protected UserInfoVO getUserInfo(PageJSON<?> pageJSON)
    {
        UserInfoVO userInfo = new UserInfoVO();
        if(null != pageJSON && null != pageJSON.getRequestAttrMap())
        {
            String userInfoJson = (String)pageJSON.getRequestAttrMap().get( Constants.USER_INFO );
            if(StringUtils.isNotBlank( userInfoJson ))
            {
                userInfo = JsonUtil.parseObject( userInfoJson, UserInfoVO.class );
            }

        }
        return userInfo;
    }

    protected UserInfoVO getUserInfo(String json)
    {
        PageJSON<String> pageJSON = getPageJSON(json, String.class);
        UserInfoVO userInfo = new UserInfoVO();
        if(null != pageJSON && null != pageJSON.getRequestAttrMap())
        {
            String userInfoJson = (String)pageJSON.getRequestAttrMap().get( Constants.USER_INFO );
            if(StringUtils.isNotBlank( userInfoJson ))
            {
                userInfo = JsonUtil.parseObject( userInfoJson, UserInfoVO.class );
            }

        }
        return userInfo;
    }


    /**
     * 获取UA
     * @param pageJSON 页面JSON字符串
     * @return 返回UA数据
     */
    protected String getUA(PageJSON<?> pageJSON)
    {
        String ua = null;
        if(null != pageJSON && null != pageJSON.getRequestAttrMap())
        {
            ua = (String)pageJSON.getRequestAttrMap().get( Constants.UA );
        }
        return ua;
    }

    /**
     * 获取IMEI 设备信息
     * @param pageJSON 页面JSON字符串
     * @return 返回UA数据
     */
    protected String getIMEI(PageJSON<?> pageJSON)
    {
        String imei = null;
        if(null != pageJSON && null != pageJSON.getRequestAttrMap())
        {
            imei = (String)pageJSON.getRequestAttrMap().get( Constants.IMEI );
        }
        return imei;
    }

    /**
     * 获取IMEI 设备信息
     * @param pageJSON 页面JSON字符串
     * @return 返回UA数据
     */
    protected String getID(PageJSON<?> pageJSON)
    {
        String imei = null;
        if(null != pageJSON && null != pageJSON.getRequestAttrMap())
        {
            imei = (String)pageJSON.getRequestAttrMap().get( Constants.ID );
        }
        return imei;
    }

    /**
     * 根据键获取attrMap的值
     * @param pageJSON 页面JSON字符串
     * @param key attrMap 键
     * @return attrMap 值
     */
    protected String getAttrMapByKey(PageJSON<?> pageJSON, String key)
    {
        String value = null;
        if(null != pageJSON && null != pageJSON.getRequestAttrMap())
        {
            value = (String)pageJSON.getRequestAttrMap().get( key );
        }
        return value;
    }

    /**
     * 根据键获取requestParameterMap的值
     * @param pageJSON 页面JSON字符串
     * @param key attrMap 键
     * @return attrMap 值
     */
    protected String getReqParameterMapByKey(PageJSON<?> pageJSON, String key)
    {
        String value = null;
        if(null != pageJSON && null != pageJSON.getRequestParameterMap())
        {
            value = pageJSON.getRequestParameterMap().get( key );
        }
        return value;
    }

    /**
     * 获取分页对象
     * @param pageJSON 页面JSON字符串
     * @param sort 排序字段
     * @return 分页对象
     */
    protected PageRequest getPageRequest(PageJSON<? extends BaseDTO> pageJSON, Sort sort)
    {
        if(null != pageJSON.getFormJSON())
        {
            BaseDTO baseDTO = pageJSON.getFormJSON();
            if(baseDTO.getPageNumber() >= 1)
            {
                pageNumber = baseDTO.getPageNumber();
            }
            if(baseDTO.getPageSize() > 0)
            {
                pageSize = baseDTO.getPageSize();
            }
        }
        return new PageRequest( pageNumber - 1, pageSize, sort );
    }

    protected <T> T getFormJSON(String json, Class<T> clz)
    {
        Object formJSOn = JsonUtil.getValueBykey( json, "formJSON" );
        if(null == formJSOn) //服务调用
        {
            return JsonUtil.parseObject( json, clz );
        }
        else //面面调用
        {
            PageJSON<T> pageJSON = getPageJSON( json, clz );
            return pageJSON.getFormJSON();
        }
    }
}
