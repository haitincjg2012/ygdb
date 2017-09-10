package com.apec.framework.controller;

import com.apec.framework.base.BaseController;
import com.apec.framework.base.IJSONService;
import com.apec.framework.common.Constants;
import com.apec.framework.common.exception.DispatchException;
import com.apec.framework.common.util.HttpRequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 类 编 号：
 * 类 名 称：PayReturnResultController
 * 内容摘要：获取支付成功之后的返回结果
 * 创建日期：2017/2/10
 * 编码作者：
 */
@RestController
public class PayReturnResultController extends BaseController
{

    private static Logger log = Logger.getLogger( PayReturnResultController.class );

    @Autowired
    private IJSONService dispatchJSONService;

    /**
     * 获取支付成功返回结果
     * @param serverName 服务名称
     * @param fileName 文件名称
     * @param methodName 调用方法名
     * @param request 请求信息
     * @return 请求返回结果
     */
    @RequestMapping(value = "/{serverName}/{fileName}/{methodName}.papec", produces = "application/json;charset=UTF-8")
    public String getPayResult(@PathVariable("serverName") String serverName, @PathVariable("fileName") String fileName,
        @PathVariable("methodName") String methodName, HttpServletRequest request)
    {
        return sendRequest( request, serverName, fileName + Constants.SINGLE_SLASH + methodName );
    }

    /**
     * 发送请求
     * @param methodName 调用方法名
     * @param serverName 服务名称
     * @return 请求返回结果
     */
    private String sendRequest(HttpServletRequest request, String serverName, String methodName)
    {
        String ret;
        try
        {
            String payResult = HttpRequestUtil.getPostJSONData( request );
            log.debug( "serverName:" + serverName + " methodName:" + methodName + " payResult:" + payResult );
            serverName = serverName + "-SERVICE";
            String serviceUrl = HttpRequestUtil.getRequestServiceUrl( serverName, methodName, StringUtils.EMPTY );
            ret = dispatchJSONService.invokeRestful( serviceUrl, payResult );
        }
        catch (DispatchException e)
        {
            return super.getResultJSONStr( false, "", Constants.SYS_ERROR );
        }
        return ret;
    }

}
