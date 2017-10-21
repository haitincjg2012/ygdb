package com.apec.user.service.impl;

import com.apec.framework.cache.CacheService;
import com.apec.framework.log.InjectLogger;
import com.apec.user.service.WeixinService;
import com.apec.user.util.SignUtil;
import com.apec.user.util.WeixinUtil;
import com.apec.user.vo.AccessToken;
import com.apec.user.vo.JsapiTicket;
import com.apec.user.vo.WeixinShareVO;
import com.netflix.discovery.converters.Auto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wubi on 2017/10/9.
 */
@Service
public class WeixinServiceImpl implements WeixinService{

    @Autowired
    private CacheService cacheService;

    @Value("${weixin_gzh_appid}")
    private String appId;

    @Value("${weixin_gzh_appsecret}")
    private String appSecret;

    @InjectLogger
    private Logger logger;

    @Override
    public WeixinShareVO getWeixinInfo(WeixinShareVO weixinShareVO) throws IOException, NoSuchAlgorithmException {
        //调用微信接口 获取数据
        //判断token是否过期 从缓存获取
        logger.info("##########get weixin token info start##########");
        String token = cacheService.get("weixin_token");
        if(StringUtils.isBlank(token) || StringUtils.isEmpty(token)) { //如果为空重新获取token
            logger.info("##########weixin token cache expired call weixin api get token##############");
            AccessToken accessToken = WeixinUtil.getAccessToken(appId, appSecret);
            cacheService.add("weixin_token", accessToken.getToken(), accessToken.getExpiresIn()/60-10);
            token = accessToken.getToken();
        }
        logger.info("##########get weixin token info end##########");


        //判断ticket是否过期 从缓存获取
        logger.info("##########get weixin ticket info start##########");
        String ticket = cacheService.get("weixin_ticket");
        if(StringUtils.isBlank(ticket) || StringUtils.isEmpty(ticket)) { //如果为空重新获取ticket
            logger.info("##########weixin ticket cache expired call weixin api get token##############");
            JsapiTicket jsapiTicket = WeixinUtil.getJsapiTicket(token);
            cacheService.add("weixin_ticket", jsapiTicket.getTicket(), jsapiTicket.getExpiresIn()/60-10);
            ticket = jsapiTicket.getTicket();
        }
        logger.info("##########get weixin ticket info end##########");


        logger.info("##########get weixin sign info start##########");
        WeixinShareVO weixinInfo = SignUtil.sign(ticket, weixinShareVO.getUrl());
        logger.info("##########get weixin sign info end##########");
        weixinInfo.setAppid(appId);
        return weixinInfo;
    }
}
