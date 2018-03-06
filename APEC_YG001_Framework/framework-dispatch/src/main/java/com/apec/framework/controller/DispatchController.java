package com.apec.framework.controller;

import com.apec.framework.base.BaseController;
import com.apec.framework.base.IJsonService;
import com.apec.framework.common.Constants;

import com.apec.framework.common.exception.DispatchException;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.mail.service.MailService;
import com.apec.framework.mail.vo.Mail;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 类 编 号：
 * 类 名 称：DispatchController
 * 内容摘要：请求分发控制器
 * 完成日期：
 * 编码作者：
 * @author xxx
 */
@RestController
public class DispatchController extends BaseController
{

    @Autowired
    private IJsonService dispatchJSONService;

    @Autowired
    private MailService mailService;

    @InjectLogger
    private Logger logger;

    @Value("${spring.profiles.active}")
    private String profile;

    @Value("${dispatch.error.sendmail}")
    private boolean sendFlag;

    /**
     * 请求分发
     * @param serverName 服务名称
     * @param methodName 调用方法名
     * @param request 请求信息
     * @return 请求返回结果
     */
    @RequestMapping(value = "/{serverName}/{methodName}.apec", produces = "application/json;charset=UTF-8")
    public String dispatchRequest(@PathVariable("serverName") String serverName,
        @PathVariable("methodName") String methodName, HttpServletRequest request)
    {
        return sendRequest( request, serverName, methodName );
    }

    /**
     * 内部接口请求分发
     * @param serverName 服务名称
     * @param methodName 调用方法名
     * @param request 请求信息
     * @return 请求返回结果
     */
    @RequestMapping(value = "/{serverName}/{methodName}.apin", produces = "application/json;charset=UTF-8")
    public String dispatchRequestIn(@PathVariable("serverName") String serverName,
                                  @PathVariable("methodName") String methodName, HttpServletRequest request)
    {
        return sendRequest( request, serverName, methodName );
    }


    /**
     * 请求分发
     * @param serverName 服务名称
     * @param methodName 调用方法名
     * @param fileName 文件名称
     * @param request 请求信息
     * @return 请求返回结果
     */
    @RequestMapping(value = "/{serverName}/{fileName}/{methodName}.apec", produces = "application/json;charset=UTF-8")
    public String dispatchRequest(@PathVariable("serverName") String serverName,
        @PathVariable("fileName") String fileName, @PathVariable("methodName") String methodName,
        HttpServletRequest request)
    {
        return sendRequest( request, serverName, fileName + Constants.SINGLE_SLASH + methodName );
    }

    /**
     * 内部接口请求分发
     * @param serverName 服务名称
     * @param methodName 调用方法名
     * @param fileName 文件名称
     * @param request 请求信息
     * @return 请求返回结果
     */
    @RequestMapping(value = "/{serverName}/{fileName}/{methodName}.apin", produces = "application/json;charset=UTF-8")
    public String dispatchRequestIn(@PathVariable("serverName") String serverName,
                                  @PathVariable("fileName") String fileName, @PathVariable("methodName") String methodName,
                                  HttpServletRequest request)
    {
        return sendRequest( request, serverName, fileName + Constants.SINGLE_SLASH + methodName );
    }

    /**
     * 发送请求
     * @param request 请求
     * @param methodName 调用方法名
     * @param serverName 服务名称
     * @return 请求返回结果
     */
    private String sendRequest(HttpServletRequest request, String serverName, String methodName)
    {
        String ret;
        try
        {
            ret = dispatchJSONService.service( serverName, methodName, request );
        }
        catch (DispatchException e)
        {
            //发生异常 发送邮件 消息格式： 环境 + 服务名称 + 方法名称 + 错误信息
            if(sendFlag) {
                String title = "call service Error  serviceName: [%s]  methodName:[%s]";
                title = String.format(title, serverName, methodName);
                String content = "Request Url: [%s] \n Erorr Message : \n %s";
                content = String.format(content, request.getRequestURI(), e.getMessage());
                Mail mail = new Mail(title, content);
                mailService.sendMail(mail, true);
                logger.error("调用服务异常，发送邮件通知 serviceName:{} methodName:{}", serverName, methodName);
            }
            return super.getResultJSONStr( false, "", e.getErrorCode() );
        }
        return ret;

    }
}
