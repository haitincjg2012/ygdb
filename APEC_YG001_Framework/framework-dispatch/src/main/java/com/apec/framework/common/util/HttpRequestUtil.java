package com.apec.framework.common.util;

import com.apec.framework.common.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public abstract class HttpRequestUtil
{
    private final static Log log = LogFactory.getLog(HttpRequestUtil.class);

    /**
     * 获取GET方式请求参数
     * @param req
     * @return
     */
    public static String getReqParam(HttpServletRequest req)
    {
        String reqStr = req.getQueryString();
        return StringUtils.isBlank(reqStr) ? StringUtils.EMPTY : Constants.QUESTION_MARK + reqStr;
    }

    /**
     * 组装get请求URL
     * @param serverName
     * @param mothodName http://COMPUTE-SERVICE/add?a=10&b=20
     * @param reqParam
     * @return
     */
    public static String getRequestServiceUrl(String serverName, String mothodName, String reqParam)
    {

        String url = Constants.HTTP_COLON + Constants.DOUBLE_SLASH + serverName.toUpperCase() + Constants.SINGLE_SLASH
                     + mothodName + reqParam;
        return url;
    }

    /**
     * 中文转码
     * @param decodeValues
     * @return
     */
    public static String decodeValues(String decodeValues)
    {
        try
        {
            return URLDecoder.decode(decodeValues, Constants.SYSTEM_ENCODING);
        }
        catch (UnsupportedEncodingException e)
        {
            log.error("It occured error in executing  RequestUtil.decodeValues,Cause:" + e.getMessage());
        }
        return null;
    }

    /**
     * 获取JSON数据
     * @param request 请求信息
     * @return
     */

    public static String getPostJSONData(HttpServletRequest request)
    {
        StringBuffer stringBuffer = new StringBuffer();
        try
        {
            BufferedReader bufferedReader = request.getReader();

            String line = null;
            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuffer.append( line );
            }

        }
        catch (IOException e)
        {
            log.error( "AbstractRequestService.getPostJSONData case:" + e.getMessage() );
        }
        return stringBuffer.toString();
    }
}
