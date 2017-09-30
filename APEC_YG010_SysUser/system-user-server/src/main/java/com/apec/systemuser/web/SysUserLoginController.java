package com.apec.systemuser.web;

import com.alibaba.fastjson.JSONObject;
import com.apec.framework.base.BaseController;
import com.apec.framework.cache.CacheException;
import com.apec.framework.cache.CacheService;
import com.apec.framework.common.Constants;
import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.PageJSON;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.constants.LoginConstants;
import com.apec.framework.common.util.JsonUtil;
import com.apec.framework.common.util.SpringUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.springcloud.SpringCloudClient;
import com.apec.systemuser.model.SysUserLoginRecord;
import com.apec.systemuser.service.SysRoleService;
import com.apec.systemuser.service.SysUserLoginRecordService;
import com.apec.systemuser.service.SysUserService;
import com.apec.systemuser.util.UUIDGenerator;
import com.apec.systemuser.vo.SysRoleVO;
import com.apec.systemuser.vo.SysUserLoginRecordVO;
import com.apec.systemuser.vo.SysUserVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 类 编 号：
 * 类 名 称：UserLoginServiceController
 * 内容摘要：请求用户信息
 * 完成日期：
 * 编码作者：
 */
@RestController
@RequestMapping("/login")
public class SysUserLoginController extends BaseController
{

    @Autowired
    private CacheService cacheService;
    
    @Autowired
    private SpringCloudClient springCloudClient;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserLoginRecordService sysUserLoginRecordService;

    @InjectLogger
    private Logger log;
    
    private static Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
    
    /**
     * token超时时间
     */
    @Value("${token.out.time}")
    private int token_out_time;
    
    /**
     * session超时时间
     */
    @Value("${session.out.time}")
    private int session_out_time;

    @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
    public String doLogin(@RequestBody String jsonStr)
    {
        log.info("login start");
        Long star = System.currentTimeMillis();
        String code = Constants.RETURN_SUCESS;
        String msg = SpringUtil.getMessage(code);
        boolean flag = true;
        PageJSON<String> pageJSON = super.getPageJSON(jsonStr, String.class);
        JSONObject formObj = JsonUtil.parseObject(pageJSON.getFormJSON(), JSONObject.class);
        log.info("step1：invalid request params");
        if(null==formObj){
            code = Constants.COMMON_MISSING_PARAMS;
            msg = SpringUtil.getMessage(code);
            flag = false;
            return getResultJSONStr(flag, null, code, msg);
        }
        String account = (String)formObj.get("userName");
        String password = (String)formObj.get("password");
        String sessionId = (String)pageJSON.getRequestAttrMap().get(Constants.SESSION_ID);
        log.info("step2：invalid login params");
        if(StringUtils.isEmpty(account)||StringUtils.isEmpty(password)){
            code = Constants.COMMON_MISSING_PARAMS;
            msg = SpringUtil.getMessage(code);
            flag = false;
            return getResultJSONStr(flag, null, code, msg);
        }
        Map<String,String> data = new HashMap<String,String>();
        try{
            log.info("step3：get user info");
            //调用用户接口
            SysUserVO dto = getFormJSON(jsonStr, SysUserVO.class);
            SysUserVO vo1 = new SysUserVO();
            vo1.setMobile(account);
            List<SysUserVO> list = sysUserService.selectAll(vo1);
            log.info("step4：check user exist");
            if(null==list||list.size()==0){
                code = ErrorCodeConst.USER_NOT_EXIST_ERROR;
                msg = SpringUtil.getMessage(code);
                return getResultJSONStr(false, null, code, msg);
            }
            log.info("step5：invalid user password");
            SysUserVO sysUser = new SysUserVO();
            sysUser = list.get(0);
            if(!dto.getPassword().equals(sysUser.getPassword())){
                code = ErrorCodeConst.PASSWORD_ERROR;
                msg = SpringUtil.getMessage(code);
                return getResultJSONStr(false, null, code, msg);
            }
            String userNo = String.valueOf(sysUser.getId());
            String name = sysUser.getName();
            String loginName = sysUser.getLoginName();
            String phone = sysUser.getMobile();
            String userRoleNo = sysUser.getUserRoleNo();
            String token = getLoginToken(userNo);
            if(!StringUtils.isEmpty(sessionId)){
                cacheService.add(LoginConstants.PREFIX_SESSIONID + sessionId, userNo, session_out_time);
            }
            log.info("step6：get role type userRoleNO: {}", userRoleNo);
            Long roleId = Long.parseLong(userRoleNo);
            if(StringUtils.isBlank(userRoleNo)){
                roleId = 0L;
            }
            //调用角色查询服务 设置角色类型
            SysRoleVO sysRoleVO = sysRoleService.findOne(roleId);
            data.put("roleType", sysRoleVO.getType());
            data.put("token", token);
            data.put("userNo", userNo);
            data.put("name", name);
            data.put("loginName", loginName);
            data.put("userRoleNo", userRoleNo);
            data.put("phone", phone);
            log.info("step7：add user info to session");
            cacheService.add(Constants.CACHE_USERINFO_PREFIX + userNo, JsonUtil.toJSONString(data));
            //sysUserLoginInfo
            SysUserLoginRecordVO sysLoginRecordVO = new SysUserLoginRecordVO();
            sysLoginRecordVO.setUserId(userNo);
            sysLoginRecordVO.setLastLoginTime(new Date());
            sysLoginRecordVO.setAccessToken(token);
            log.info("insert sysuser login record : {}", sysLoginRecordVO);
            sysUserLoginRecordService.insert(sysLoginRecordVO, userNo);
            Long end = System.currentTimeMillis();
            log.error("doLogin use time end-star : " + (end - star));
        }catch (Exception e){
            code = Constants.SYS_ERROR;
            msg = SpringUtil.getMessage(code);
            flag = false;
            log.error("调用后台服务异常", e);
        }
        log.info("login end");
        return getResultJSONStr(flag, data, code, msg);
    }
    
    /**
     * 请求其他服务
     * @param server
     * @param method
     * @param reqMap
     * @return
     */
    private ResultData callServer(String server, String method, Map<String,String> reqMap){
        ResultData resultData = null;
        String url = Constants.HTTP_COLON + Constants.DOUBLE_SLASH + server + Constants.SINGLE_SLASH + method;
        try{
            String res = springCloudClient.post(url, JsonUtil.toJSONString(reqMap));
            resultData = JsonUtil.parseObject(res, ResultData.class);
        }catch (Exception e){
            log.error("调用后台服务异常 " + url, e);
            resultData = getResultData(false, null, Constants.SYS_ERROR);
        }
        return resultData;
    }
    
    private String getLoginToken(String userNo)
    {
        final String token = UUIDGenerator.getToken();

        // 清除之前用户id的 token
        Object oldToken = cacheService.get(LoginConstants.PREFIX_TOKEN_USERNO + userNo);
        if (null != oldToken)
            cacheService.remove(LoginConstants.PREFIX_TOKEN + oldToken);

        // 保存token 到redis
        cacheService.add(LoginConstants.PREFIX_TOKEN_USERNO + userNo, token, token_out_time*24*60); // (userNo , token)

        cacheService.add(LoginConstants.PREFIX_TOKEN + token, userNo, token_out_time*24*60); // (token , userNo)

        return token;
    }
    
    @RequestMapping(value = "/loginOut", method = RequestMethod.POST)
    public String loginOut(@RequestBody String jsonStr)
    {
        String code = Constants.RETURN_SUCESS;
        String msg = SpringUtil.getMessage(code);
        boolean flag = true;
        PageJSON<String> pageJSON = super.getPageJSON(jsonStr, String.class);
        String sessionId = (String)pageJSON.getRequestAttrMap().get(Constants.SESSION_ID);
        //JSONObject formObj = JsonUtil.parseObject(pageJSON.getFormJSON(), JSONObject.class);
        if(StringUtils.isEmpty(sessionId)){
            code = Constants.COMMON_MISSING_PARAMS;
            msg = SpringUtil.getMessage(code);
            flag = false;
            log.error("loginOut 错误" + jsonStr);
            return getResultJSONStr(flag, null, code, msg);
        }
          cacheService.remove(LoginConstants.PREFIX_SESSIONID + sessionId);
        return getResultJSONStr(flag, null, code, msg);
    }
    
    @RequestMapping(value = "/validateToken", method = RequestMethod.POST)
    public String validateToken(@RequestBody String jsonStr){
        PageJSON<SysUserLoginRecord> pageJSON = super.getPageJSON(jsonStr, SysUserLoginRecord.class);
        String userNo = pageJSON.getRequestParameterMap().get("userNo");
        String token = pageJSON.getRequestParameterMap().get("token");
        String code = Constants.RETURN_SUCESS;
        String msg = SpringUtil.getMessage(code);
        boolean flag = true;
        boolean login = false;
        try{
            if(!StringUtils.isEmpty(userNo)&&!StringUtils.isEmpty(token)){
                String redis_token = (String)cacheService.get(LoginConstants.PREFIX_TOKEN_USERNO + userNo);
                if(redis_token.equals(token))
                    login = true;
            }
        }catch (CacheException e){
            code = Constants.SYS_ERROR;
            msg = SpringUtil.getMessage(code);
            flag = false;
            log.error("调用后台服务异常", e);
        }
        return getResultJSONStr(flag, login, code, msg);
    }
}
