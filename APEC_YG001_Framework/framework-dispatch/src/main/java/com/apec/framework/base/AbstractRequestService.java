package com.apec.framework.base;

import com.alibaba.fastjson.JSONObject;
import com.apec.framework.common.Constants;
import com.apec.framework.common.util.AbstractHttpRequestUtil;
import com.apec.framework.common.util.BaseJsonUtil;
import com.apec.framework.common.util.AbstractJssbUtil;
import com.apec.framework.common.util.BaseStringUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 类 编 号：
 * 类 名 称：AbstractRequestService
 * 内容摘要：抽象请求类,用于处理页面传递过来的request
 * 完成日期：
 * 编码作者：
 * @author xxx
 */
public abstract class AbstractRequestService
{
    private static Logger log = Logger.getLogger( AbstractRequestService.class );

    private final String FIRST_ROLE_TYPE = "1";

    /**
     * 前置空方法,扩展类实现
     */
    protected void preHandle(HttpServletRequest request)
    {

    }

    /**
     * 后置空方法,扩展类实现
     */
    protected void afterCompletion(HttpServletRequest request)
    {

    }

    /**
     * 解析所有request请求属性并缓存
     * @param request 请求信息
     */
    protected Map<String, Object> parseRequestAttribute(HttpServletRequest request)
    {
        // 取出所有参数
        Map<String, Object> requestAttrMap = new HashMap<>(16);
        try
        {
            Enumeration<String> paramNames = request.getAttributeNames();
            if(null != paramNames)
            {
                while (paramNames.hasMoreElements())
                {
                    String attrName = paramNames.nextElement();
                    if(!attrName.contains( "." ))
                    {
                        Object attrValue = request.getAttribute( attrName );
                        requestAttrMap.put( attrName, attrValue );
                    }
                }
            }
            else
            {
                log.info( "Request request without any attribute!" );
            }
        }
        catch (Exception e)
        {
            log.error( "It occured error in executing  AbstractRequestService.parseRequestAttribute,Cause:"
                       + e.getMessage() );
        }
        parseSessionAttribute( request, requestAttrMap );
        return requestAttrMap;
    }

    /**
     * 解析session中属性并缓存
     * @param request 请求信息
     */
    private void parseSessionAttribute(HttpServletRequest request, Map<String, Object> requestAttrMap)
    {
        try
        {
            HttpSession httpSession = request.getSession();
            if(null != httpSession)
            {
                Enumeration paramNames = httpSession.getAttributeNames();
                if(null != paramNames)
                {
                    while (paramNames.hasMoreElements())
                    {
                        String attrName = (String)paramNames.nextElement();
                        Object attrValue = httpSession.getAttribute( attrName );
                        requestAttrMap.put( attrName, attrValue );
                    }
                }
            }
            else
            {
                log.info( "No attribute at session!" );
            }
        }
        catch (Exception e)
        {
            log.error( "It occured error in executing  AbstractRequestService.parseRequestAttribute,Cause:"
                       + e.getMessage() );
        }
    }

    /**
     *
     * @param request 经过spring处理过的request，请求输入流已经没有数据
     * @return formJson里面的数据
     */
    protected String parseRequest(HttpServletRequest request)
    {
        //表单方式请求
        Map<String, String[]> parameterMap = request.getParameterMap();
        // uri后面的参数
        Map<String, Object> uriMap = new HashMap<>(16);
        // 流数据，此处数据格式是json
        String requestJson = null;
        if(null != parameterMap && !parameterMap.isEmpty())
        {
            for(Map.Entry<String, String[]> entry : parameterMap.entrySet())
            {
                String key = entry.getKey();
                String[] value = entry.getValue();
                if(null == requestJson && null != key && key.startsWith( "{" ) && key
                    .endsWith( "}" ))
                {
                    // 判断官方经过转换的流式json
                    requestJson = key;
                }
                else
                {
                    if(null != value && value.length == 1)
                    {
                        uriMap.put( key, value[0] );
                    }
                    else if(null != value && value.length > 1)
                    {
                        uriMap.put( key, value );
                    }
                }
            }
        }

        // 纯json请求处理
        if(Constants.APPLICATION_JSON.equalsIgnoreCase( request.getContentType() ))
        {
            //获取流数据
            requestJson = AbstractHttpRequestUtil.getPostJSONData( request );
            log.info( "流数据JSON格式：" + requestJson );
        }
        Map<String, Object> attrMap = parseRequestAttribute( request );
        
        //客户类型的角色控制数据权限
        if(null!=attrMap){
        	Object obj = attrMap.get(Constants.USER_INFO);
        	if(null!=obj){
        		JSONObject json = BaseJsonUtil.parseObject(obj.toString());
        		String roleType = json.getString("roleType");
        		if(FIRST_ROLE_TYPE.equals(roleType)){
        			if(null==requestJson||com.apec.framework.common.StringUtil.isEmpty(requestJson)){
        				Map<String,String> orgMap = new HashMap<>(16);
        				orgMap.put("orgCode", json.getString("orgCode"));
        				requestJson = BaseJsonUtil.toJSONString( orgMap );
        			}else{
        				JSONObject tem = BaseJsonUtil.parseObject(requestJson);
        				String orgCode = tem.getString("orgCode");
        				if(null==orgCode || com.apec.framework.common.StringUtil.isEmpty(orgCode)){
        					tem.put("orgCode", json.getString("orgCode"));
        				}
        				requestJson = BaseJsonUtil.toJSONString( tem );
        			}
        		}
        	}
        }
        
        String pageJson = AbstractJssbUtil.assemblRequestJSON( requestJson, BaseJsonUtil.toJSONString( uriMap ),
                                                       BaseJsonUtil.toJSONString( attrMap ) );
        attrMap = null;
        uriMap = null;
        parameterMap = null;
        return pageJson;
    }


    /**
     * 描述:获取 post 请求的 byte[] 数组
     * <pre>
     * 举例：
     * </pre>
     * @param request request
     * @return byte[]
     * @throws IOException io异常
     */
    private static byte[] getRequestPostBytes(HttpServletRequest request)
        throws IOException
    {
        int contentLength = request.getContentLength();
        if(contentLength < 0)
        {
            return null;
        }
        byte[] buffer = new byte[contentLength];
        for(int i = 0; i < contentLength; )
        {

            int readlen = request.getInputStream().read( buffer, i,
                                                         contentLength - i );
            if(readlen == -1)
            {
                break;
            }
            i += readlen;
        }
        return buffer;
    }

    /**
     * 描述:获取 post 请求内容
     * <pre>
     * 举例：
     * </pre>
     * @param request request
     * @return String
     */
    public static String getRequestPostStr(HttpServletRequest request)
    {
        try
        {
            byte[] buffer = getRequestPostBytes( request );
            String charEncoding = request.getCharacterEncoding();
            if(charEncoding == null)
            {
                charEncoding = "UTF-8";
            }
            if(buffer != null){
                return new String( buffer, charEncoding );
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 取出分页表起始页页码
     * @return int 起始页页码
     */
    protected int getPageNum()
    {
        String pageNum = Constants.DEFAULT_RANGESTART  ;
        return BaseStringUtil.convertStringToInt( pageNum );
    }

    /**
     * 取出分页表每页容纳数据条数
     * @return 数据条数
     */
    protected int getPageSize()
    {
        String pageSize = Constants.DEFAULT_FETCHSIZE;
        return BaseStringUtil.convertStringToInt( pageSize );
    }

    
    /**
    *
    * @param request 经过spring处理过的request，请求输入流已经没有数据
    * @return formJson里面的数据
    */
   protected Map<String, Object> parseRequestGet(HttpServletRequest request)
   {
       //表单方式请求
       Map<String, String[]> parameterMap = request.getParameterMap();
       // uri后面的参数
       Map<String, Object> uriMap = new HashMap<>(16);
       if(null != parameterMap && !parameterMap.isEmpty())
       {
           for(Map.Entry<String, String[]> entry : parameterMap.entrySet())
           {
               String key = entry.getKey();
               String[] value = entry.getValue();
                   if(null != value && value.length == 1)
                   {
                       uriMap.put( key, value[0] );
                   }
                   else if(null != value && value.length > 1)
                   {
                       uriMap.put( key, value );
                   }
           }
       }
      return uriMap;
   }
   
   
}
