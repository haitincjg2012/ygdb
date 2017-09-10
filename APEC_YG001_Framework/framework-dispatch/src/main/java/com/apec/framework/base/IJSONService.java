package com.apec.framework.base;

import javax.servlet.http.HttpServletRequest;

public interface IJSONService
{
    /**
     * 发送rest服务到后台
     * @param serviceUrl 服务uri
     * @param jsonStr 请求json
     */
    String invokeRestful(String serviceUrl, String jsonStr);

    /**
     * 处理页面过来的请求
     * @param serverName 请求服务名称
     * @param methodName 请求服务方法
     * @param req 请求信息
     * @return 请求结果
     */
    String service(String serverName, String methodName,HttpServletRequest req);
}
