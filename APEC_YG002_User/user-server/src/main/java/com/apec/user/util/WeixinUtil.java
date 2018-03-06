package com.apec.user.util;

import com.alibaba.fastjson.JSONObject;
import com.apec.framework.common.util.BaseJsonUtil;
import com.apec.user.vo.AccessToken;
import com.apec.user.vo.JsapiTicket;

import java.io.IOException;

/**
 * Created by wubi on 2017/10/9.
 * @author wubi
 */
public class WeixinUtil {

    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    private static final String JSAPI_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

    public static JsapiTicket getJsapiTicket(String token) throws  IOException {
        String url = JSAPI_TICKET_URL.replace("ACCESS_TOKEN", token);
        JSONObject jsonObject = doGetStr(url);
        JsapiTicket jsapiTicket = new JsapiTicket();
        if (jsonObject != null) {
            jsapiTicket.setTicket(jsonObject.getString("ticket"));
            jsapiTicket.setExpiresIn(jsonObject.getInteger("expires_in"));
        }
        return jsapiTicket;
    }

    public static AccessToken getAccessToken(String appId, String appSecret) throws IOException {
        AccessToken token = new AccessToken();
        String url = ACCESS_TOKEN_URL.replace("APPID", appId).replace("APPSECRET", appSecret);
        JSONObject jsonObject = doGetStr(url);
        if(jsonObject!=null){
            token.setToken(jsonObject.getString("access_token"));
            token.setExpiresIn(jsonObject.getInteger("expires_in"));
        }
        return token;
    }

    private static JSONObject doGetStr(String url) throws IOException {
        HttpClientUtil httpClientUtil = HttpClientUtil.getInstance();
        String res = httpClientUtil.sendHttpGet(url);
        return BaseJsonUtil.parseObject(res);
    }
}
