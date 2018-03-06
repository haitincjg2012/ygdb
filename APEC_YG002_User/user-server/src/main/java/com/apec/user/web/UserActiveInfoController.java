package com.apec.user.web;

import com.apec.framework.common.Constants;
import com.apec.framework.log.InjectLogger;
import com.apec.user.service.UserActiveInfoService;
import com.apec.user.vo.UserActiveInfoVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by hmy on 2018/2/7.
 *
 * @author hmy
 */
@RestController
@RequestMapping("/userActiveInfo")
public class UserActiveInfoController extends MyBaseController {

    @InjectLogger
    private Logger log;

    @Autowired
    private UserActiveInfoService userActiveInfoService;

    /**
     * 增加活动生成名片信息记录
     */
    @RequestMapping(value = "/addUserActiveInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String addUserActiveInfo(@RequestBody String json) {
        try{
            UserActiveInfoVO userActiveInfoVO = getFormJSON(json,UserActiveInfoVO.class);
            String result = userActiveInfoService.addUserActiveInfo(userActiveInfoVO);
            if(StringUtils.equals(result, Constants.RETURN_SUCESS)){
                return super.getResultJSONStr(true, userActiveInfoVO, null);
            }else{
                return super.getResultJSONStr(false, null, result);
            }
        } catch (Exception e) {
            log.error("[userActiveInfo][addUserActiveInfo] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }

    }

    /**
     * 生成名片记录
     */
    @RequestMapping(value = "/updateUserActiveInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String updateUserActiveInfo(@RequestBody String json) {
        try{
            UserActiveInfoVO userActiveInfoVO = getFormJSON(json,UserActiveInfoVO.class);
            boolean flag = userActiveInfoVO == null || StringUtils.isBlank(userActiveInfoVO.getActiveUrl());
            if(flag){
                return super.getResultJSONStr(true, null, Constants.ERROR_100003);
            }
            userActiveInfoVO.setCreateActiveUrlDate(new Date());
            String result = userActiveInfoService.updateUserActiveInfo(userActiveInfoVO);
            if(StringUtils.equals(result, Constants.RETURN_SUCESS)){
                return super.getResultJSONStr(true, userActiveInfoVO, null);
            }else{
                return super.getResultJSONStr(false, null, result);
            }
        } catch (Exception e) {
            log.error("[userActiveInfo][updateUserActiveInfo] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }

    }

    /**
     * 生成名片记录
     */
    @RequestMapping(value = "/findUserActiveInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String findUserActiveInfo(@RequestBody String json) {
        try{
            UserActiveInfoVO userActiveInfoVO = getFormJSON(json,UserActiveInfoVO.class);
            boolean flag = userActiveInfoVO == null || userActiveInfoVO.getId() == null || userActiveInfoVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(true, null, Constants.ERROR_100003);
            }
            return super.getResultJSONStr(true, userActiveInfoService.findUserActiveInfo(userActiveInfoVO), null);
        } catch (Exception e) {
            log.error("[userActiveInfo][findUserActiveInfo] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }

    }

}
