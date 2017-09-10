package com.apec.user.web;

import com.apec.framework.cache.CacheService;
import com.apec.framework.common.Constants;
import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.PageJSON;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.constants.LoginConstants;
import com.apec.framework.common.enums.Enums;
import com.apec.framework.common.enums.Source;
import com.apec.framework.common.util.JsonUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.user.service.UserService;
import com.apec.user.vo.UserLoginVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Title: 用户登陆
 *
 * @author yirde  2017/7/11.
 */
@RestController
@RequestMapping("/login")
public class UserLoginController  extends MyBaseController {

    @InjectLogger
    private Logger log;

    /**
     * 用户服务
     */
    @Autowired
    private UserService userService;

    /**
     * token超时时间
     */
    @Value("${token.out.time}")
    private int token_out_time;

    /**
     * 缓存服务
     */
    @Autowired
    private CacheService cacheService;

    /**
     *  登陆
     * @param jsonStr 参数
     * @return String
     */
    @RequestMapping(value = "/doLogin", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<Map<String,String>> doLogin(@RequestBody String jsonStr){
        ResultData<Map<String,String>> resultData = new ResultData<>();

        PageJSON<String> pageJSON = super.getPageJSON(jsonStr, String.class);
        UserLoginVO userLoginVO = JsonUtil.parseObject(pageJSON.getFormJSON(), UserLoginVO.class);
        userLoginVO.setIp((String)pageJSON.getRequestAttrMap().get(Constants.SESSION_IP));
        String source = (String) pageJSON.getRequestAttrMap().get(Constants.SOURCE);
        Source sourceType = Source.WEIXIN;
        if(StringUtils.isNotBlank(source)) sourceType = Enums.getEnumByNameOrNull(Source.class,source);
        userLoginVO.setSource(sourceType);
        log.info("[UserLogin][doLogin]UserLoginVO:mobile:{},ip:{},source:{}",userLoginVO.getMobile(),userLoginVO.getIp());
        boolean flag = StringUtils.isBlank(userLoginVO.getMobile()) && StringUtils.isBlank(userLoginVO.getPassword());
        if(flag){
            log.warn("[UserLogin][doLogin] user mobile or password or sessionID is blank, please check and retry!");
            setErrorResultDate(resultData,ErrorCodeConst.ERROR_USER_LOGIN_FAILED);
            return resultData;
        }
        try {
            Long star = System.currentTimeMillis();
            Map<String,String> resultMap = new HashMap<>();
            String returnCode = userService.quickLogin(userLoginVO,token_out_time,resultMap,false);
            Long end = System.currentTimeMillis();
            log.error("[UserLogin][doLogin] Use Time : " + (end - star));
            if(StringUtils.equals(returnCode,Constants.RETURN_SUCESS)){
                resultData.setData(resultMap);
                resultData.setSucceed(true);
            }else{
                setErrorResultDate(resultData,returnCode);
            }
        }catch (Exception e){
            log.error("[UserLogin][doLogin] Exception", e);
            setErrorResultDate(resultData,Constants.SYS_ERROR);
        }
        return resultData;
    }

    /**
     *  退出登录
     * @param jsonStr 参数
     * @return String
     */
    @RequestMapping(value = "/loginOut", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<Map<String,String>> loginOut(@RequestBody String jsonStr){
        ResultData<Map<String,String>> resultData = new ResultData<>();
        Long userId = getUserId(jsonStr);
        try {
            //清除用户token
            String token = cacheService.get(LoginConstants.PREFIX_TOKEN_USERNO + userId);
            if (StringUtils.isNotBlank(token))
                cacheService.remove(LoginConstants.PREFIX_TOKEN + token);
            resultData.setSucceed(true);
        }catch (Exception e){
            log.error("[UserLogin][loginOut] Exception", e);
            setErrorResultDate(resultData,Constants.SYS_ERROR);
        }
        return resultData;
    }


}
