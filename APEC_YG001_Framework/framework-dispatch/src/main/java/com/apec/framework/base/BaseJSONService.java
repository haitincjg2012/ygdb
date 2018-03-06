package com.apec.framework.base;

import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.exception.DispatchException;
import com.apec.framework.common.tools.UuidGenerator;
import com.apec.framework.common.util.AbstractHttpRequestUtil;
import com.apec.framework.common.util.BaseJsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * 类 编 号：
 * 类 名 称：BaseJsonService
 * 内容摘要：服务请求分发基类,其它特殊要求处理的必须继承此类
 * 完成日期：
 * 编码作者：
 * @author xx xx
 */
public abstract class BaseJsonService extends AbstractRequestService implements IJsonService
{
    @Autowired
    private RestTemplate restTemplate;

    private static Logger log = Logger.getLogger( BaseJsonService.class );

    /**
     * 处理页面过来的请求
     * @param req 请求信息
     * @return 请求返回结果
     */
    @Override
    public String service(String serverName, String methodName, HttpServletRequest req)
    {
        String jsonStr = super.parseRequest( req );
        String serviceUrl = AbstractHttpRequestUtil.getRequestServiceUrl( serverName, methodName, StringUtils.EMPTY );
        log.info( "serviceUrl:" + serviceUrl + ",jsonStr:" + jsonStr );
        preHandle( req );
        String ret;
        try
        {
            ret = invokeRestful( serviceUrl, jsonStr );
        }
        catch (Exception e)
        {
            log.error(
                "call serviceUrl:" + serviceUrl + ",serverName:" + serverName + ",methodName:" + methodName + ",Cause:"
                + e );
            throw new DispatchException( ErrorCodeConst.REQ_SERVER_EXCEPTION, e.getMessage() );
        }
        afterCompletion( req );
        //返回重复提交的值
        ret = setRepeatAct( req, ret );
        log.info( "serviceUrl:" + serviceUrl + ",ret:" + ret );
        return ret;
    }

    /**
     * 设置重复提交的值
     * @param req 请求信息
     * @param ret 返回信息
     */
    private String setRepeatAct(HttpServletRequest req, String ret)
    {
        ResultData resultData = BaseJsonUtil.parseObject( ret, ResultData.class );
        resultData.setRepeatAct( UuidGenerator.getUuidWithLine() );
        ret = BaseJsonUtil.toJSONString( resultData );
        return ret;
    }

    /**
     * 发送rest服务到后台
     * @param serviceUrl 服务uri
     * @param jsonStr 请求json
     * @return 请求返回结果
     */
    @Override
    public String invokeRestful(String serviceUrl, String jsonStr)
    {
        //TODO 此处后续可能要做处理
        return restTemplate.postForObject( serviceUrl, jsonStr, String.class );
    }
}
