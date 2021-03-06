package com.apec.user.web;

import com.alibaba.fastjson.JSONObject;
import com.apec.framework.cache.CacheService;
import com.apec.framework.common.Constants;
import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.PageJSON;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.enums.Enums;
import com.apec.framework.common.enums.Source;
import com.apec.framework.common.excel.ExcelExportUtils;
import com.apec.framework.common.excel.XlsVO;
import com.apec.framework.common.exception.BusinessException;
import com.apec.framework.common.util.BaseJsonUtil;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.common.util.FileUtils;
import com.apec.framework.common.util.ValidateUtil;
import com.apec.framework.ftp.service.FtpService;
import com.apec.framework.log.InjectLogger;
import com.apec.user.dto.UserAuthRecordDTO;
import com.apec.user.dto.UserDTO;
import com.apec.user.service.UserService;
import com.apec.user.vo.QuotationUser;
import com.apec.user.vo.UserAllInfo;
import com.apec.user.vo.UserAuthRecordVO;
import com.apec.user.vo.UserAuthRecordViewVO;
import com.apec.user.vo.UserLoginVO;
import com.apec.user.vo.UserOrgClientDTO;
import com.apec.user.vo.UserOrgClientVO;
import com.apec.user.vo.UserVO;
import com.apec.user.vo.UserViewVO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * 用户模块接口
 *
 * @author yirder
 */
@RestController
@RequestMapping("/user")
public class UserController extends MyBaseController {

    @InjectLogger
    private Logger log;

    @Autowired
    private UserService userService;

    @Autowired
    private CacheService redisCacheService;

    @Autowired
    private FtpService ftpService;

    @Value("${EXCELFILEPATH}")
    String excelPath = "";

    @Value("${ORG_EXCELFILEPATH}")
    String orgExcelPath = "";

    @Value("${EXCELFILE_URL}")
    String excelUrl = "";

    /**
     * token超时时间
     */
    @Value("${token.out.time}")
    private int token_out_time;

    /**
     * 注册新用户
     * @param json 参数集
     * @return String 结果集
     */
    @RequestMapping(value = "/addNewUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String addNewUser(@RequestBody String json) {
        PageJSON<String> pageJSON = super.getPageJSON(json, String.class);
        try {
            //获取数据并验证数据的有效性
            UserVO userVO = getFormJSON(json,UserVO.class);

            //参数验证
            boolean flag = StringUtils.isBlank(userVO.getMobile()) || StringUtils.isBlank(userVO.getPassword()) ||
                    StringUtils.isBlank(userVO.getVaildCode()) || userVO.getUserType() == null;
            if(flag) { return super.getResultJSONStr(false, null, Constants.COMMON_MISSING_PARAMS); }

            //密码长度不小于6位
            if(userVO.getPassword().length() < Integer.valueOf(Constants.PASSWORD_MIN_LENGTH)){
                return super.getResultJSONStr(false,null, ErrorCodeConst.ERROR_PASSWORD);
            }
            //Captcha校验,一次性校验,无需两次
            String key = Constants.CACHE_CAPTCHA_PREFIX + userVO.getMobile();
            String yzm = redisCacheService.get(key);
            log.info("[user][addNewUser]Get Captcha：{}" , yzm);
            if(StringUtils.isBlank(yzm)){
                return super.getResultJSONStr(false,null,ErrorCodeConst.ERROR_VAILDCODE);
            }
            if(!StringUtils.equals(yzm,userVO.getVaildCode())){
                return super.getResultJSONStr(false,null,ErrorCodeConst.ERROR_VAILDCODE);
            }
            redisCacheService.remove(key);
            //去除缓存中该用户的验证码信息，更新信息

            //Source判定
            String source = (String) pageJSON.getRequestAttrMap().get(Constants.SOURCE);
            Source sourceType = Source.WEIXIN;
            if(StringUtils.isNotBlank(source)) {
                sourceType = Enums.getEnumByNameOrNull(Source.class,source);
            }
            userVO.setSource(sourceType);
            //注册用户信息
            String result = userService.addNewUser(userVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                //注册成功，自动登录
                Map<String,String> resultMap = new HashMap<>(16);
                UserLoginVO userLoginVO = new UserLoginVO();
                userLoginVO.setMobile(userVO.getMobile());
                userLoginVO.setPassword(userVO.getPassword());
                userLoginVO.setIp((String)pageJSON.getRequestAttrMap().get(Constants.SESSION_IP));
                userLoginVO.setSource(sourceType);
                //快速登陆
                userService.quickLogin(userLoginVO,token_out_time,resultMap,true);
                return super.getResultJSONStr(true, resultMap, "");
            }else{
                return super.getResultJSONStr(false, null, result);
            }
        } catch (Exception e) {
            log.error("[user][addNewUser] Exception : {}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }

    }

    /**
     * 上传头像
     */
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String uploadImage(@RequestBody String json) {
        String result = Constants.SYS_ERROR;
        Map<String,String> data = new HashMap<>(16);
        try{
            UserVO userVO = getFormJSON(json,UserVO.class);
            //判断用户是否重新上传了图片，是则保存新上传的图片路径
            if(userVO == null || StringUtils.isBlank(userVO.getImgUrl())){
                return super.getResultJSONStr(true, data, ErrorCodeConst.NOT_IMG);
            }
            userVO.setId(getUserId(json));
            result = userService.updateImage(userVO,String.valueOf(getUserId(json)),data);
        } catch (Exception e) {
            log.error("[user][uploadImage] Exception：{}", e);
        }
        if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
            return super.getResultJSONStr(true, data, null);
        }else{
            return super.getResultJSONStr(false, null, result);
        }
    }

    /**
     * 上传用户banner图
     */
    @RequestMapping(value = "/uploadBanner", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String uploadBanner(@RequestBody String json) {
        String result = Constants.SYS_ERROR;
        Map<String,String> data = new HashMap<>(16);
        try{
            UserVO userVO = getFormJSON(json,UserVO.class);
            //判断用户是否重新上传了图片，是则保存新上传的图片路径
            if(userVO == null || StringUtils.isBlank(userVO.getImgUrl())){
                return super.getResultJSONStr(true, data, ErrorCodeConst.NOT_IMG);
            }
            userVO.setId(getUserId(json));
            //判断用户是否重新上传了图片，是则保存新上传的图片路径
            UserOrgClientVO userOrgClientVO = new UserOrgClientVO();
            userOrgClientVO.setOrgFirstBannerUrl(userVO.getImgUrl());
            userOrgClientVO.setOrgBannerUrl(userVO.getImgUrl());
            userVO.setUserOrgClientVO(userOrgClientVO);
            //用户上传banner图
            result = userService.updateBanner(userVO,String.valueOf(getUserId(json)),data);

        } catch (Exception e) {
            log.error("[User][UploadBanner] Exception：{}", e);
        }
        if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
            return super.getResultJSONStr(true, data, null);
        }else{
            return super.getResultJSONStr(false, null, result);
        }
    }

    /**
     * 设置二维码图片地址
     */
    @RequestMapping(value = "/setQrCodeUrl", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String setQrCodeUrl(@RequestBody String json) {
        String result = Constants.SYS_ERROR;
        Map<String,String> data = new HashMap<>(16);
        try{
            UserVO userVO = getFormJSON(json,UserVO.class);
            //判断用户是否重新上传了图片，是则保存新上传的图片路径
            if(userVO == null || StringUtils.isBlank(userVO.getQrCodeUrl())){
                return super.getResultJSONStr(true, data, Constants.ERROR_100003);
            }
            userVO.setId(getUserId(json));
            result = userService.setQrCodeUrl(userVO,String.valueOf(getUserId(json)),data);

        } catch (Exception e) {
            log.error("[User][UploadBanner] Exception：{}", e);
        }
        if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
            return super.getResultJSONStr(true, data, null);
        }else{
            return super.getResultJSONStr(false, null, result);
        }
    }

    /**
     * 修改用户信息
     */
    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String updateUserInfo(@RequestBody String json) {
        Map<String,String> data = new HashMap<>(16);
        try {
            //获取前端用户传来的需修改的信息
            UserVO userVO = getFormJSON(json,UserVO.class);
            if(userVO == null){
                return super.getResultJSONStr(true,null,Constants.ERROR_100003);
            }
            //如果为前台传来的信息，form中没有用户id，将RequestAttrMap中的用户id赋予用户对象
            if(userVO.getId() == null || userVO.getId() == 0L){
                Long userId = getUserId(json);
                userVO.setId(userId);
            }
            //修改用户信息
            String result = userService.updateUserInfo(userVO,String.valueOf(getUserId(json)),data);

            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                return super.getResultJSONStr(true, data, "");
            }else{
                return super.getResultJSONStr(false, null, result);
            }
        } catch (Exception e) {
            log.error("[user][updateUserInfo]  Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 用户实名认证
     */
    @RequestMapping(value = "/userRealName", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String userRealName(@RequestBody String json){
        UserAuthRecordVO authRecordVO = getFormJSON(json, UserAuthRecordVO.class);
        String result = Constants.SYS_ERROR;
        //获取前端用户传来的实名验证相关信息
        Long userNo = this.getUserId(json);
        authRecordVO.setUserId(userNo);
        boolean flag = authRecordVO == null || StringUtils.isBlank(authRecordVO.getIdNumber()) || StringUtils.isBlank(authRecordVO.getRealName());
        if(flag){
            //参数不能为空（身份证号和用户真实姓名）
            return super.getResultJSONStr(false,null,Constants.ERROR_100003);
        }
        String idNumber = authRecordVO.getIdNumber().trim();
        if(!ValidateUtil.checkIdNumber(idNumber)){
            //身份证正则匹配不通过
            return super.getResultJSONStr(false,null,ErrorCodeConst.ERROR_IDNUMBER);
        }
        if(StringUtils.isBlank(authRecordVO.getImgOneURL()) || StringUtils.isBlank(authRecordVO.getImgTwoURL())){
            //参数不能为空,必须上传两张身份证证件照
            return super.getResultJSONStr(false,null,ErrorCodeConst.NOT_IMG);
        }
        Map<String,String> data = new HashMap<>(16);
        try {
            //请求实名认证用户信息
            result = userService.userRealNameApply(authRecordVO,String.valueOf(getUserId(json)),data);

        } catch (Exception e) {
            log.error("[user][userRealName] Exception：{}", e);
        }
        if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
            return super.getResultJSONStr(true, data, "");
        }else{
            return super.getResultJSONStr(false, null, result);
        }
    }

    /**
     * 实名认证记录查询（分页）
     */
    @RequestMapping(value = "/pageUserRealNameRecord", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String pageUserRealNameRecord(@RequestBody String json){
        //获取前端用户传来的实名验证相关信息
        UserAuthRecordDTO dto = getFormJSON(json,UserAuthRecordDTO.class);
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "success"));
        orders.add(new Sort.Order(Sort.Direction.DESC, "createDate"));
        int pageNumber = 1;
        int pageSize = 10;
        if (dto.getPageNumber() > 0) {
            pageNumber = dto.getPageNumber();
        }
        if (dto.getPageSize() > 0 && dto.getPageSize() < Integer.valueOf(Constants.MAX_FETCHSIZE)) {
            pageSize = dto.getPageSize();
        }
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, new Sort(orders));
        try {
            PageDTO<UserAuthRecordViewVO> page = userService.pageUserRealNameRecord(dto,pageRequest);
            return super.getResultJSONStr(true, page, "");
        } catch (Exception e) {
            log.error("[user][pageUserRealNameRecord] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 实名认证审批
     */
    @RequestMapping(value = "/userRealNameEntrue", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String userRealNameEntrue(@RequestBody String json){
        String result = Constants.SYS_ERROR;
        //获取前端用户传来的实名验证相关信息
        UserAuthRecordVO authRecordVO = getFormJSON(json,UserAuthRecordVO.class);
        if(authRecordVO == null || authRecordVO.getId() == null || authRecordVO.getId() == 0L){
            return super.getResultJSONStr(false,null,Constants.ERROR_100003);
        }
        //参数校验
        boolean flag = !StringUtils.equals(authRecordVO.getSuccess(),Constants.ISSUCCESS) && !StringUtils.equals(authRecordVO.getSuccess(),Constants.ISNOTSUCCESS);
        if(flag){
            //审批结果不为Y也不为N，有误，不能是其他结果，参数有误
            return super.getResultJSONStr(false,null,Constants.COMMON_ERROR_PARAMS);
        }
        Map<String,String> data = new HashMap<>(16);
        try {
            result = userService.userRealNameEntrue(authRecordVO,String.valueOf(getUserId(json)),data);
        } catch (Exception e) {
            log.error("[user][userRealNameEntrue] Exception：{}", e);
        }
        if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
            return super.getResultJSONStr(true, data, "");
        }else{
            return super.getResultJSONStr(false, null, result);
        }
    }

    /**
     * 找回密码(设置新密码)
     */
    @RequestMapping(value = "/getPassword",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String getPassword(@RequestBody String json){
        String result = Constants.SYS_ERROR;
        //获取前端用户传来的实名验证相关信息

        UserVO userVO = getFormJSON(json,UserVO.class);
        if(userVO == null){
            return super.getResultJSONStr(true,null,Constants.ERROR_100003);
        }

        boolean flag = StringUtils.isBlank(userVO.getPassword()) || StringUtils.isBlank(userVO.getEntruePassword()) || StringUtils.isBlank(userVO.getMobile());
        if(flag){
            //输入密码或确认密码为空，不允许通过,手机号不能为空
            return super.getResultJSONStr(false,null,Constants.ERROR_100003);
        }
        //密码长度不小于6位
        if(userVO.getPassword().length() < Integer.valueOf(Constants.PASSWORD_MIN_LENGTH)){
            return super.getResultJSONStr(false,null, ErrorCodeConst.ERROR_PASSWORD);
        }
        if(!StringUtils.equals(userVO.getPassword(),userVO.getEntruePassword())){
            //两次输入密码不一致
            return super.getResultJSONStr(false,null,ErrorCodeConst.PASSWORD_NOT_EQUAL);
        }
        try {
            //修改表中密码
            result = userService.getPassword(userVO,String.valueOf(getUserId(json)));
        } catch (Exception e) {
            log.error("[user][getPassword] Exception：{}", e);
        }
        if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
            return super.getResultJSONStr(true, null, "");
        }else{
            return super.getResultJSONStr(false, null, result);
        }
    }

    /**
     * 修改密码
     */
    @RequestMapping(value = "/updatePassword",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String updatePassword(@RequestBody String json){
        String result = Constants.SYS_ERROR;
        //获取前端用户传来的实名验证相关信息
        Long userId = getUserId(json);
        UserVO userVO = getFormJSON(json,UserVO.class);
        if(userVO == null){
            return super.getResultJSONStr(true,null,Constants.ERROR_100003);
        }
        //如果为前台传来的信息，form中没有用户id，将RequestAttrMap中的用户id赋予用户对象
        if(userVO.getId() == null || userVO.getId() == 0L){
            userVO.setId(userId);
        }
        boolean flag = StringUtils.isBlank(userVO.getOldPassword()) || StringUtils.isBlank(userVO.getPassword()) || StringUtils.isBlank(userVO.getEntruePassword());
        if(flag){
            //输入密码或确认密码为空，不允许通过,验证码不能为空
            return super.getResultJSONStr(false,null,Constants.ERROR_100003);
        }
        //密码长度不小于6位
        if(userVO.getPassword().length() < Integer.valueOf(Constants.PASSWORD_MIN_LENGTH)){
            return super.getResultJSONStr(false,null, ErrorCodeConst.ERROR_PASSWORD);
        }
        if(!StringUtils.equals(userVO.getPassword(),userVO.getEntruePassword())){
            //两次输入密码不一致
            return super.getResultJSONStr(false,null,ErrorCodeConst.PASSWORD_NOT_EQUAL);
        }
        try {
            //修改表中密码
            result = userService.updatePassword(userVO,String.valueOf(getUserId(json)));
        } catch (Exception e) {
            log.error("[user][updatePassword] Exception：{}", e);
        }
        if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
            return super.getResultJSONStr(true, null, "");
        }else{
            return super.getResultJSONStr(false, null, result);
        }
    }


    /**
     * 更换手机号
     */
    @RequestMapping(value = "/updateMobile",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String updateMobile(@RequestBody String json){
        String result = Constants.SYS_ERROR;
        //获取前端用户传来的实名验证相关信息
        Long userId = getUserId(json);
        UserVO userVO = getFormJSON(json,UserVO.class);
        if(userVO == null){
            return super.getResultJSONStr(true,null,Constants.ERROR_100003);
        }
        //如果为前台传来的信息，form中没有用户id，将RequestAttrMap中的用户id赋予用户对象
        if(userVO.getId() == null || userVO.getId() == 0L){
            userVO.setId(userId);
        }
        boolean flag = StringUtils.isBlank(userVO.getMobile()) || StringUtils.isBlank(userVO.getVaildCode());
        if(flag){
            //输入手机号为空或输入验证码为空则不允许
            return super.getResultJSONStr(false,null,Constants.ERROR_100003);
        }
        Map<String,String> resultMap = new HashMap<>(16);
        try {
            String key = Constants.CACHE_CAPTCHA_PREFIX + userVO.getMobile();
            String yzm = redisCacheService.get(key);
            if(StringUtils.isBlank(yzm)){
                return super.getResultJSONStr(false,null,ErrorCodeConst.ERROR_VAILDCODE);
            }
            if(!StringUtils.equals(yzm,userVO.getVaildCode())){
                return super.getResultJSONStr(false,null,ErrorCodeConst.ERROR_VAILDCODE);
            }
            //去除缓存中该用户的验证码信息，更新信息
            redisCacheService.remove(key);
            ///验证码通过则修改保存的该用户的手机号
            result = userService.updateUserMobile(userVO,String.valueOf(getUserId(json)),resultMap);
        } catch (Exception e) {
            log.error("[user][updateMobile] Exception：{}", e);
        }
        if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
            return super.getResultJSONStr(true, resultMap, "");
        }else{
            return super.getResultJSONStr(false, null, result);
        }
    }

    /**
     * 手机号是否已经注册
     */
    @RequestMapping(value = "/isHasMobile",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String isHasMobile(@RequestBody String json){
        String result = Constants.SYS_ERROR;
        boolean isHas = false;
        //获取前端用户传来的实名验证相关信息
        UserVO userVO = getFormJSON(json, UserVO.class);
        if(userVO == null){
            //用户不能为空
            return super.getResultJSONStr(false,null,ErrorCodeConst.USER_NOTNULL);
        }
        if(StringUtils.isBlank(userVO.getMobile())){
            //输入手机号为空不允许
            return super.getResultJSONStr(false,null,Constants.ERROR_100003);
        }
        if(!ValidateUtil.checkMobile(userVO.getMobile())){
            //手机号不合法
            return super.getResultJSONStr(false,null,ErrorCodeConst.ERROR_MOBILE);
        }
        try {
            isHas = userService.isExist(userVO);
            result = Constants.RETURN_SUCESS;
        } catch (Exception e) {
            log.error("[user][isHasMobile] Exception：{}", e);
        }
        if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
            return super.getResultJSONStr(true, isHas, "");
        }else{
            return super.getResultJSONStr(false, null, result);
        }
    }

    /**
     * 用户信息查询（分页）
     */
    @RequestMapping(value = "/pageUserInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String pageUserInfo(@RequestBody String json){
        //获取前端用户传来的实名验证相关信息
        UserDTO dto = getFormJSON(json,UserDTO.class);
        PageRequest pageRequest = genPageRequest(dto);
        try {
            PageDTO<UserAllInfo> page = userService.pageUserInfo(dto,pageRequest);
            return super.getResultJSONStr(true, page, "");
        } catch (Exception e) {
            log.error("[user][pageUserInfo] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 删除用户信息
     */
    @RequestMapping(value = "/deleteUserInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String deleteUserInfo(@RequestBody String json) {
        Map<String,String> data = new HashMap<>(16);
        try {
            UserVO userVO = getFormJSON(json,UserVO.class);
            if(userVO == null || userVO.getId() == null || userVO.getId() == 0L){
                return super.getResultJSONStr(true,null,Constants.ERROR_100003);
            }
            //修改用户信息
            String result = userService.deleteUserInfo(userVO,String.valueOf(getUserId(json)));

            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                return super.getResultJSONStr(true, data, "");
            }else{
                return super.getResultJSONStr(false, null, result);
            }
        } catch (Exception e) {
            log.error("[user][deleteUserInfo]  Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 批量删除用户信息
     */
    @RequestMapping(value = "/deleteUserInfoList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String deleteUserInfoList(@RequestBody String json) {
        try {
            UserVO userVO = getFormJSON(json,UserVO.class);
            if(userVO == null || userVO.getIds() == null || userVO.getIds().size() <= 0){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            userService.deleteUserList(userVO.getIds(),String.valueOf(getUserId(json)));
            return super.getResultJSONStr(true, null, "");
        } catch (Exception e) {
            log.error("[user][deleteUserInfoList]  Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 查询用户信息
     */
    @RequestMapping(value = "/findUserInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String findUserInfo(@RequestBody String json) {
        try {
            UserVO userVO = getFormJSON(json, UserVO.class);
            if(userVO == null){
                userVO = new UserVO();
            }
            if (userVO.getId() == null || userVO.getId() == 0L) {
                userVO.setId(getUserId(json));
            }
            UserViewVO viewVO = userService.findUserInfo(userVO);
            return super.getResultJSONStr(true, viewVO, "");

        } catch (Exception e) {
            log.error("[user][findUserInfo]  Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 查询用户信息
     */
    @RequestMapping(value = "/queryUserInfoByMobile", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String queryUserInfoByMobile(@RequestBody String json) {
        try {
            UserVO userVO = getFormJSON(json, UserVO.class);
            if (userVO == null || StringUtils.isBlank(userVO.getMobile())) {
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            return super.getResultJSONStr(true, userService.queryUserInfoByMobile(userVO), null);

        } catch (Exception e) {
            log.error("[user][queryUserInfoByMobile]  Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 查询用户自己的信息
     */
    @RequestMapping(value = "/findSelfInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String findSelfInfo(@RequestBody String json) {
        try {
            UserVO userVO = new UserVO();
            userVO.setId(getUserId(json));

            UserViewVO viewVO = userService.findUserInfo(userVO);
            return super.getResultJSONStr(true, viewVO, null);

        } catch (Exception e) {
            log.error("[user][findSelfInfo]  Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 查询用户信息id
     */
    @RequestMapping(value = "/listUserId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String listUserId(@RequestBody String json) {
        try {
            UserVO userVO = getFormJSON(json, UserVO.class);
            List<Long> data = userService.listUserId(userVO);
            return super.getResultJSONStr(true, data, "");

        } catch (Exception e) {
            log.error("[user][listUserId]  Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 用户信息查询
     */
    @RequestMapping(value = "/listUserInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String listUserInfo(@RequestBody String json){
        UserVO vo = getFormJSON(json,UserVO.class);
        try {
            List<UserViewVO> users  = userService.listUserInfo(vo);
            return super.getResultJSONStr(true, users, null);
        } catch (Exception e) {
            log.error("[user][listUserInfo] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 所有组织账号信息查询
     */
    @RequestMapping(value = "/findOrgList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String findOrgList(){
        try {
            List<UserOrgClientVO> userOrgClientVOS  = userService.findOrgList();
            return super.getResultJSONStr(true, userOrgClientVOS, null);
        } catch (Exception e) {
            log.error("[user][findOrgList] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 分页查询所有的账户信息
     */
    @RequestMapping(value = "/pageUserOrg", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String pageUserOrg(@RequestBody String json){
        //获取前端用户传来的实名验证相关信息
        UserOrgClientDTO dto = getFormJSON(json,UserOrgClientDTO.class);
        PageRequest pageRequest = genPageRequest(dto);
        try {
            PageDTO<UserOrgClientVO> page = userService.findOrgPage(pageRequest, dto);
            return super.getResultJSONStr(true, page, "");
        } catch (Exception e) {
            log.error("[user][pageUserOrg] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 推送用户与组织，设置用户账户信息
     */
    @RequestMapping(value = "/pushUserAndOrg", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String pushUserAndOrg(@RequestBody String json){
        UserVO userVO = getFormJSON(json, UserVO.class);
        try {
            boolean flag = userVO == null || userVO.getId() == null || userVO.getId() == 0L;
            if(flag) {
                return super.getResultJSONStr(false, null, Constants.COMMON_MISSING_PARAMS);
            }
            String result = userService.pushUserAndOrg(userVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(Constants.RETURN_SUCESS,result)){
                return super.getResultJSONStr(true, null, null);
            }else{
                return super.getResultJSONStr(false, null, result);
            }

        } catch (Exception e) {
            log.error("[user][pushUserAndOrg] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 设置组织标签
     */
    @RequestMapping(value = "/setOrgTags", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String setOrgTags(@RequestBody String json){
        UserOrgClientVO userOrgClientVO = getFormJSON(json,UserOrgClientVO.class);
        try {
            if(userOrgClientVO == null || userOrgClientVO.getId() == null || userOrgClientVO.getId() == 0L){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            String result = userService.setOrgTags(userOrgClientVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(Constants.RETURN_SUCESS,result)){
                return super.getResultJSONStr(true, null, "");
            }else{
                return super.getResultJSONStr(false, null, result);
            }

        } catch (Exception e) {
            log.error("[user][setOrgTags] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 删除组织
     */
    @RequestMapping(value = "/deleteOrg", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String deleteOrg(@RequestBody String json){
        UserOrgClientVO userOrgClientVO = getFormJSON(json,UserOrgClientVO.class);
        try {
            if(userOrgClientVO == null || userOrgClientVO.getId() == null || userOrgClientVO.getId() == 0L){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            String result = userService.deleteOrg(userOrgClientVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(Constants.RETURN_SUCESS,result)){
                return super.getResultJSONStr(true, null, null);
            }else{
                return super.getResultJSONStr(false, null, result);
            }

        } catch (Exception e) {
            log.error("[user][deleteOrg] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 修改组织信息
     */
    @RequestMapping(value = "/updateOrg", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String updateOrg(@RequestBody String json){
        UserOrgClientVO userOrgClientVO = getFormJSON(json,UserOrgClientVO.class);
        try {
            if(userOrgClientVO == null || userOrgClientVO.getId() == null || userOrgClientVO.getId() == 0L){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            String result = userService.updateOrg(userOrgClientVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(Constants.RETURN_SUCESS,result)){
                return super.getResultJSONStr(true, null, null);
            }else{
                return super.getResultJSONStr(false, null, result);
            }

        } catch (Exception e) {
            log.error("[user][updateOrg] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 修改组织信息
     */
    @RequestMapping(value = "/findUserByOrgId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String findUserByOrgId(@RequestBody String json){
        UserVO userVO = getFormJSON(json,UserVO.class);
        try {
            if(userVO.getIds() == null || userVO.getId() == 0L){
                userVO.setId(getUserId(json));
            }
            List<UserViewVO> userViewVOS = userService.findUserByOrgId(userVO);
            return super.getResultJSONStr(true, userViewVOS, null);

        } catch (Exception e) {
            log.error("[user][findUserByOrgId] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 我的关注(组织账号信息)
     */
    @RequestMapping(value = "/findUserFocusOrg", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String findUserFocusOrg(@RequestBody String json){
        try {
            UserDTO userDTO = getFormJSON(json,UserDTO.class);
            PageRequest pageRequest = genPageRequest(userDTO);
            PageDTO<UserAllInfo> userViewVOS = userService.findUserFocusOrg(getUserId(json),pageRequest,userDTO);
            return super.getResultJSONStr(true, userViewVOS, null);
        } catch (Exception e) {
            log.error("[user][findUserFocusOrg] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 用户解除绑定组织
     */
    @RequestMapping(value = "/unBoundOrg", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String unBoundOrg(@RequestBody String json){
        try {
            UserVO userVO = getFormJSON(json,UserVO.class);
            boolean flag = userVO == null || userVO.getId() == null || userVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false, null, Constants.COMMON_MISSING_PARAMS);
            }
            String result = userService.unBoundOrg(userVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                return super.getResultJSONStr(true, null, null);
            }else{
                return super.getResultJSONStr(false, null, result);
            }

        } catch (Exception e) {
            log.error("[user][unBoundOrg] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 查询用户组织具体信息
     */
    @RequestMapping(value = "/findUserOrgInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String findUserOrgInfo(@RequestBody String json){
        try {
            UserOrgClientVO userOrgClientVO = getFormJSON(json,UserOrgClientVO.class);
            boolean flag = userOrgClientVO == null || userOrgClientVO.getId() == null || userOrgClientVO.getId() == 0L;
            if(flag) {
                return super.getResultJSONStr(false, null, Constants.COMMON_MISSING_PARAMS);
            }
            userOrgClientVO = userService.findUserOrgInfo(userOrgClientVO);
            return super.getResultJSONStr(true,userOrgClientVO, null);

        } catch (Exception e) {
            log.error("[user][findUserOrgInfo] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }
    /**
     * 查询用户组织具体信息
     */
    @RequestMapping(value = "/findUserOrgName", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String findUserOrgName(@RequestBody String json){
        try {
            UserOrgClientVO userOrgClientVO = getFormJSON(json,UserOrgClientVO.class);
            boolean flag = userOrgClientVO == null || userOrgClientVO.getId() == null || userOrgClientVO.getId() == 0L;
            if(flag) {
                return super.getResultJSONStr(false, null, Constants.COMMON_MISSING_PARAMS);
            }
            String orgName = userService.findUserOrgName(userOrgClientVO.getId());
            return super.getResultJSONStr(true,orgName, null);

        } catch (Exception e) {
            log.error("[user][findUserOrgInfo] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }


    /**
     * 根据index重建索引 冷库
     */
    @RequestMapping(value = "/pushDepotEsInfoByIndex", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<String> pushDepotEsInfoByIndex(@RequestBody String json){
        ResultData<String> resultData = getResultData(true, null, Constants.RETURN_SUCESS);
        try{
            PageJSON<String> pageJSON = super.getPageJSON(json, String.class);
            JSONObject formObj = BaseJsonUtil.parseObject(pageJSON.getFormJSON(), JSONObject.class);
            String indexUrl = (String) BaseJsonUtil.getValueBykey(json,"indexUrl");
            if(formObj!= null) {
                indexUrl = (String)formObj.get("indexUrl");
            }
            String returnCode = userService.pushDepotEsInfoByIndex(indexUrl);
            if(!Constants.RETURN_SUCESS.equals(returnCode)) {
                setErrorResultDate(resultData, returnCode);
            }
        }catch (Exception e){
            log.error("[UserController] [pushEsInfoByIndex] Exception", e);
            setErrorResultDate(resultData,Constants.SYS_ERROR);
        }
        return resultData;
    }

    /**
     * 根据index重建索引 代办
     */
    @RequestMapping(value = "/pushAgencyEsInfoByIndex", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<String> pushAgencyEsInfoByIndex(@RequestBody String json){
        ResultData<String> resultData = getResultData(true, null, Constants.RETURN_SUCESS);
        try{
            PageJSON<String> pageJSON = super.getPageJSON(json, String.class);
            JSONObject formObj = BaseJsonUtil.parseObject(pageJSON.getFormJSON(), JSONObject.class);
            String indexUrl = (String) BaseJsonUtil.getValueBykey(json,"indexUrl");
            if(formObj!= null) {
                indexUrl = (String)formObj.get("indexUrl");
            }
            String returnCode = userService.pushAgencyEsInfoByIndex(indexUrl);
            if(!Constants.RETURN_SUCESS.equals(returnCode)) {
                setErrorResultDate(resultData, returnCode);
            }
        }catch (Exception e){
            log.error("[UserController] [pushEsInfoByIndex] Exception", e);
            setErrorResultDate(resultData,Constants.SYS_ERROR);
        }
        return resultData;
    }

    /**
     * 根据index重建索引 客商
     */
    @RequestMapping(value = "/pushMerchantEsInfoByIndex", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<String> pushMerchantEsInfoByIndex(@RequestBody String json){
        ResultData<String> resultData = getResultData(true, null, Constants.RETURN_SUCESS);
        try{
            PageJSON<String> pageJSON = super.getPageJSON(json, String.class);
            JSONObject formObj = BaseJsonUtil.parseObject(pageJSON.getFormJSON(), JSONObject.class);
            String indexUrl = (String) BaseJsonUtil.getValueBykey(json,"indexUrl");
            if(formObj!= null) {
                indexUrl = (String)formObj.get("indexUrl");
            }
            String returnCode = userService.pushMerchantEsInfoByIndex(indexUrl);
            if(!Constants.RETURN_SUCESS.equals(returnCode)) {
                setErrorResultDate(resultData, returnCode);
            }
        }catch (Exception e){
            log.error("[UserController] [pushEsInfoByIndex] Exception", e);
            setErrorResultDate(resultData,Constants.SYS_ERROR);
        }
        return resultData;
    }
    /**
     * 关闭认证
     */
    @RequestMapping(value = "/closeOrgPushFlag", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String closeOrgPushFlag(@RequestBody String json){
        try {
            UserVO userVO = getFormJSON(json,UserVO.class);
            boolean flag = userVO == null || userVO.getId() == null || userVO.getId() == 0L;
            if(flag) {
                return super.getResultJSONStr(false, null, Constants.COMMON_MISSING_PARAMS);
            }
            String result = userService.closeOrgPushFlag(userVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(Constants.RETURN_SUCESS,result)){
                return super.getResultJSONStr(true,null, null);
            }else{
                return super.getResultJSONStr(false,null, result);
            }


        } catch (Exception e) {
            log.error("[user][closeOrgPushFlag] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 后台代注册新用户
     * @param json 参数集
     * @return String 结果集
     */
    @RequestMapping(value = "/addNewUserLast", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String addNewUserLast(@RequestBody String json) {
        try {
            //获取数据并验证数据的有效性
            UserVO userVO = getFormJSON(json,UserVO.class);
            //参数验证
            boolean flag = userVO == null || StringUtils.isBlank(userVO.getMobile())
                    || userVO.getUserType() == null;
            if(flag) {
                return super.getResultJSONStr(false, null, Constants.COMMON_MISSING_PARAMS);
            }
            if(StringUtils.isBlank(userVO.getPassword())){
                //设置默认密码
                userVO.setPassword(Constants.DEFAULT_PASSWORD);
            }
            //密码长度不小于6位
            if(userVO.getPassword().length() < Integer.valueOf(Constants.PASSWORD_MIN_LENGTH)){
                return super.getResultJSONStr(false,null, ErrorCodeConst.ERROR_PASSWORD);
            }
            Source sourceType = Source.WEIXIN;
            if(userVO.getSource() == null){
                userVO.setSource(sourceType);
            }
            //注册用户信息
            String result = userService.addNewUser(userVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                return super.getResultJSONStr(true, null, null);
            }else{
                return super.getResultJSONStr(false, null, result);
            }
        } catch (Exception e) {
            log.error("[user][addNewUserLast] Exception : {}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }

    }

    /**
     * 批量认证
     */
    @RequestMapping(value = "/batchAuthen", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String batchAuthen(@RequestBody String json) {
        try {
            //获取数据并验证数据的有效性
            UserVO userVO = getFormJSON(json,UserVO.class);
            //参数验证
            boolean flag = userVO == null || userVO.getIds() == null;
            if(flag) {
                return super.getResultJSONStr(false, null, Constants.COMMON_MISSING_PARAMS);
            }

            //注册用户信息
            String result = userService.batchAuthen(userVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                return super.getResultJSONStr(true, null, null);
            }else{
                return super.getResultJSONStr(false, null, result);
            }
        } catch (BusinessException e) {
            log.error("[user][batchAuthen] BusinessException : batch authentication user org exception ,the caused is {}", e.getMessage());
            return super.getResultJSONStr(false, null, e.getMessage());
        } catch (Exception e) {
            log.error("[user][batchAuthen] Exception : {}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }

    }

    /**
     * 批量认证
     */
    @RequestMapping(value = "/queryQuotationUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String queryQuotationUser(@RequestBody String json) {
        try {
            //获取数据并验证数据的有效性
            UserDTO userDTO = getFormJSON(json, UserDTO.class);
            PageRequest pageRequest = genPageRequest(userDTO);
            QuotationUser quotationUser = getFormJSON(json,QuotationUser.class);
            return super.getResultJSONStr(true, userService.quotationUser(quotationUser,pageRequest), null);
        } catch (BusinessException e) {
            log.error("[user][queryQuotationUser] BusinessException : queryQuotationUser exception ,the caused is {}", e.getMessage());
            return super.getResultJSONStr(false, null, e.getMessage());
        } catch (Exception e) {
            log.error("[user][queryQuotationUser] Exception : {}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }

    }

    /**
     * 处理果满仓用户信息
     */
    @RequestMapping(value = "/userOfWmsDeal", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String userOfWmsDeal(@RequestBody String json) {
        try {
            //获取数据并验证数据的有效性
            UserVO userVO = getFormJSON(json, UserVO.class);
            String result = userService.userOfWmsDeal(userVO.getList());
            if(StringUtils.equals(Constants.RETURN_SUCESS,result)){
                return super.getResultJSONStr(true,userVO.getList() , null);
            }else{
                return super.getResultJSONStr(false, null, result);
            }

        } catch (BusinessException e) {
            log.error("[user][userOfWmsDeal] BusinessException : userOfWmsDeal exception ,the caused is {}", e.getMessage());
            return super.getResultJSONStr(false, null, e.getMessage());
        } catch (Exception e) {
            log.error("[user][userOfWmsDeal] Exception : {}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }

    }

    /**
     * 导出用户信息
     */
    @RequestMapping(value = "/exportExcel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String exportExcelUserInfo(@RequestBody String json) {
        UserDTO dto = getParamJSON(json,UserDTO.class);
        try {
            String[] excelHeader = new String[] { "编号", "昵称","电话", "身份", "企业名称","客户类型","账户类型","认证标识","等级","积分","创建时间","账号状态"};
            String fileName = ExcelExportUtils.getExcelFileName("user");
            List<Object[]> results = userService.selectUserInfoForExcel(dto);
            String filePath = FileUtils.getFileRelativePath(excelPath);
            ByteArrayOutputStream os = ExcelExportUtils.exportExcel(excelHeader, results, false);
            byte[] b = os.toByteArray();
            ByteArrayInputStream in = new ByteArrayInputStream(b);
		    ftpService.uploadFile(filePath, fileName, in);
            XlsVO xlsVO = new XlsVO();
            xlsVO.setFileName(fileName);
            xlsVO.setUrl(excelUrl +filePath + fileName);
            return super.getResultJSONStr(true, xlsVO, null);
        } catch (Exception e) {
            log.error("[user][exportExcel] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }

    }

    /**
     * 导出企业信息
     */
    @RequestMapping(value = "/exportExcelOrg", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String exportExcelOrg(@RequestBody String json) {
        UserOrgClientDTO dto = getParamJSON(json,UserOrgClientDTO.class);
        try {
            String[] excelHeader = new String[] { "编号", "企业名称","企业类型","区域","详细地址","销售区域","主营品种","拥有的客户","标签","实力描述","创建时间"};
            List<Object[]> results = userService.selectUserOrgForExcel(dto);
            String fileName = ExcelExportUtils.getExcelFileName("userOrg");
            String filePath = FileUtils.getFileRelativePath(orgExcelPath);
            ByteArrayOutputStream os = ExcelExportUtils.exportExcel(excelHeader, results, false);
            byte[] b = os.toByteArray();
            ByteArrayInputStream in = new ByteArrayInputStream(b);
            ftpService.uploadFile(filePath, fileName, in);
            XlsVO xlsVO = new XlsVO();
            xlsVO.setFileName(fileName);
            xlsVO.setUrl(excelUrl +filePath + fileName);
            return super.getResultJSONStr(true, xlsVO, null);
        } catch (Exception e) {
            log.error("[user][exportExcelOrg] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 是否完善用户信息，提醒三次
     */
    @RequestMapping(value = "/checkFullUserInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String checkFullUserInfo(@RequestBody String json) {
        try {
            Boolean result = userService.checkFullUserInfo(getUserId(json));
            if(result != null && result){
                return super.getResultJSONStr(true, true, null);
            }else{
                return super.getResultJSONStr(true, false, null);
            }

        } catch (Exception e) {
            log.error("[user][checkFullUserInfo] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 查询我邀请的用户信息（分页）
     */
    @RequestMapping(value = "/queryMyInviteUserInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String queryMyInviteUserInfo(@RequestBody String json) {
        UserDTO userDTO = getFormJSON(json,UserDTO.class);
        try {
            PageRequest pageRequest = genPageRequest(userDTO);
            return super.getResultJSONStr(true, userService.queryInviteUserInfo(pageRequest,getUserId(json)), null);

        } catch (Exception e) {
            log.error("[user][queryMyInviteUserInfo] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 查询某用户邀请的用户信息（分页）
     */
    @RequestMapping(value = "/queryInviteUserInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String queryInviteUserInfo(@RequestBody String json) {
        UserDTO userDTO = getFormJSON(json,UserDTO.class);
        try {
            PageRequest pageRequest = genPageRequest(userDTO);
            return super.getResultJSONStr(true, userService.queryInviteUserInfo(pageRequest,userDTO.getId()), null);

        } catch (Exception e) {
            log.error("[user][queryInviteUserInfo] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }


}
