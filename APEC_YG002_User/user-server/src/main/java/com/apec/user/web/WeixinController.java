package com.apec.user.web;

import com.apec.framework.common.Constants;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.util.BaseJsonUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.user.service.WeixinService;
import com.apec.user.vo.WeixinShareVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wubi on 2017/10/9.
 * @author xxx
 */
@RestController
@RequestMapping(value="/wxapi")
public class WeixinController extends MyBaseController{

    @InjectLogger
    private Logger logger;

    @Autowired
    private WeixinService service;

    /**
     * 微信分享
     */
    @RequestMapping(value="/getSignInfo",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public ResultData addNewUserPoint(@RequestBody String json){
        //获取数据
        WeixinShareVO weixinShareVO = getFormJSON(json,WeixinShareVO.class);
        try{
            //判断接口地址是否为空
            if(weixinShareVO == null || StringUtils.isBlank(weixinShareVO.getUrl())){
                logger.error("url param is null ");
                return super.getResultData(false, null, Constants.ERROR_100003);
            }
            //获取信息
            weixinShareVO = service.getWeixinInfo(weixinShareVO);
            logger.info("weixin info :{}", BaseJsonUtil.toJSONString(weixinShareVO));
            return getResultData(true,weixinShareVO,Constants.RETURN_SUCESS);
        }catch(Exception e){
            logger.error("[WeixinController][share] exception:{}" , e);
            return getResultData(false,null,Constants.SYS_ERROR);
        }


    }
}
