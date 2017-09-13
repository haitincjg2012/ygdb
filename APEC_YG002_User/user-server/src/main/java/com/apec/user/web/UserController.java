package com.apec.user.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.apec.framework.cache.CacheHashService;
import com.apec.framework.cache.CacheService;
import com.apec.framework.common.*;
import com.apec.framework.common.enums.Enums;
import com.apec.framework.common.enums.Source;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.common.util.JsonUtil;
import com.apec.framework.common.util.ValidateUtil;
import com.apec.framework.dto.ImageUploadVO;
import com.apec.user.dto.UserDTO;
import com.apec.user.model.UserTags;
import com.apec.user.service.UserService;
import com.apec.user.vo.*;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import com.apec.framework.log.InjectLogger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    private CacheHashService cacheHashService;

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
            if(flag) return super.getResultJSONStr(false, null, Constants.COMMON_MISSING_PARAMS);

            //密码长度不小于6位
            if(userVO.getPassword().length() < 6){
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
            redisCacheService.remove(key);//去除缓存中该用户的验证码信息，更新信息

            //Source判定
            String source = (String) pageJSON.getRequestAttrMap().get(Constants.SOURCE);
            Source sourceType = Source.WEIXIN;
            if(StringUtils.isNotBlank(source)) sourceType = Enums.getEnumByNameOrNull(Source.class,source);
            userVO.setSource(sourceType);
            //注册用户信息
            String result = userService.addNewUser(userVO);
            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                //注册成功，自动登录
                Map<String,String> resultMap = new HashMap<>();
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
        Map<String,String> data = new HashMap<>();
        try{
            UserVO userVO = new UserVO();
            PageJSON<Object> pageJSON = getPageJSON(json, Object.class);
            String userNo = getUserNo(pageJSON);
            userVO.setId(NumberUtils.toLong(userNo));
            JSONArray imageListJsonArray = (JSONArray)pageJSON.getRequestAttrMap().get("imageItems");
            List<ImageUploadVO> imageUploadVOS = new ArrayList<>();
            if(null != imageListJsonArray && imageListJsonArray.size() >0){
                imageUploadVOS = JsonUtil.parseArray(imageListJsonArray.get(0).toString(),ImageUploadVO.class);
            }
            String imageUrl = "";
            if(null != imageUploadVOS && imageUploadVOS.size() >0){
                imageUrl = imageUploadVOS.get(0).getImagePath();
            }
            //判断用户是否重新上传了图片，是则保存新上传的图片路径
            if(!StringUtils.isBlank(imageUrl)){
                userVO.setImgUrl(imageUrl);
                result = userService.updateImage(userVO,String.valueOf(getUserId(json)),data);
            }
        } catch (Exception e) {
            log.error("[user][uploadImage] Exception：{}", e);
        }
        if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
            return super.getResultJSONStr(true, data, "");
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
        Map<String,String> data = new HashMap<>();
        try{
            UserVO userVO = new UserVO();
            PageJSON<Object> pageJSON = getPageJSON(json, Object.class);
            String userNo = getUserNo(pageJSON);
            userVO.setId(NumberUtils.toLong(userNo));
            JSONArray imageListJsonArray = (JSONArray)pageJSON.getRequestAttrMap().get(Constants.IMAGE_ITEMS);
            List<ImageUploadVO> imageUploadVOS = new ArrayList<>();
            if(null != imageListJsonArray && imageListJsonArray.size() >0){
                imageUploadVOS = JsonUtil.parseArray(imageListJsonArray.get(0).toString(),ImageUploadVO.class);
            }
            String imageUrl = "";
            String firstImageUrl = "";
            if(null != imageUploadVOS && imageUploadVOS.size() >0){
                firstImageUrl = imageUploadVOS.get(0).getImagePath();
                imageUrl = imageUploadVOS.get(1).getImagePath();
            }
            //判断用户是否重新上传了图片，是则保存新上传的图片路径
            if(!StringUtils.isBlank(imageUrl)){
                UserOrgClientVO userOrgClientVO = new UserOrgClientVO();
                userOrgClientVO.setOrgFirstBannerUrl(firstImageUrl);
                userOrgClientVO.setOrgBannerUrl(imageUrl);
                userVO.setUserOrgClientVO(userOrgClientVO);//用户上传banner图
                result = userService.updateBanner(userVO,String.valueOf(getUserId(json)),data);
            }
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
     * @param json
     * @return
     */
    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String updateUserInfo(@RequestBody String json) {
        Map<String,String> data = new HashMap<>();
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
        String result = Constants.SYS_ERROR;
        //获取前端用户传来的实名验证相关信息
        UserAuthRecordViewVO viewVO = getFormJSON(json, UserAuthRecordViewVO.class);
        UserVO userVO = new UserVO();
        if(viewVO != null){
            userVO.setRealName(viewVO.getRealName());
            userVO.setIdNumber(viewVO.getIdNumber());
        }
        Long userNo = this.getUserId(json);
        userVO.setId(userNo);
        boolean flag = StringUtils.isBlank(userVO.getIdNumber()) || StringUtils.isBlank(userVO.getRealName());
        if(flag){
            //参数不能为空（身份证号和用户真实姓名）
            return super.getResultJSONStr(false,null,Constants.ERROR_100003);
        }
        String idNumber = userVO.getIdNumber().trim();
        if(!ValidateUtil.checkIdNumber(idNumber)){
            //身份证正则匹配不通过
            return super.getResultJSONStr(false,null,ErrorCodeConst.ERROR_IDNUMBER);
        }
        UserAuthRecordVO authRecordVO = new UserAuthRecordVO();
        BeanUtil.copyPropertiesIgnoreNullFilds(viewVO,authRecordVO);
        authRecordVO.setUserVO(userVO);
        if(StringUtils.isBlank(authRecordVO.getImgOneURL()) || StringUtils.isBlank(authRecordVO.getImgTwoURL())){
            //参数不能为空,必须上传两张身份证证件照
            return super.getResultJSONStr(false,null,ErrorCodeConst.NOT_IMG);
        }
        Map<String,String> data = new HashMap<>();
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
        PageDTO<UserAuthRecordViewVO> page = new PageDTO<>();
        //获取前端用户传来的实名验证相关信息
        UserDTO dto = getFormJSON(json,UserDTO.class);
        PageRequest pageRequest = genPageRequest(dto);
        try {
            page = userService.pageUserRealNameRecord(dto,pageRequest);
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
        Map<String,String> data = new HashMap<>();
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
        //如果为前台传来的信息，form中没有用户id，将RequestAttrMap中的用户id赋予用户对象
//        if(userVO.getId() == null || userVO.getId() == 0L){
//            Long userId = getUserId(json);
//            userVO.setId(userId);
//        }
        boolean flag = StringUtils.isBlank(userVO.getPassword()) || StringUtils.isBlank(userVO.getEntruePassword()) || StringUtils.isBlank(userVO.getVaildCode()) || StringUtils.isBlank(userVO.getMobile());
        if(flag){
            //输入密码或确认密码为空，不允许通过,手机号和验证码不能为空
            return super.getResultJSONStr(false,null,Constants.ERROR_100003);
        }
        //密码长度不小于6位
        if(userVO.getPassword().length() < 6){
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
        if(userVO.getPassword().length() < 6){
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
        Map<String,String> resultMap = new HashMap<>();
        try {
            String key = Constants.CACHE_CAPTCHA_PREFIX + userVO.getMobile();
            String yzm = redisCacheService.get(key);
            if(StringUtils.isBlank(yzm)){
                return super.getResultJSONStr(false,null,ErrorCodeConst.ERROR_VAILDCODE);
            }
            if(!StringUtils.equals(yzm,userVO.getVaildCode())){
                return super.getResultJSONStr(false,null,ErrorCodeConst.ERROR_VAILDCODE);
            }
            redisCacheService.remove(key);//去除缓存中该用户的验证码信息，更新信息
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
        PageDTO<UserViewVO> page = new PageDTO<>();
        //获取前端用户传来的实名验证相关信息
        UserDTO dto = getFormJSON(json,UserDTO.class);
        PageRequest pageRequest = genPageRequest(dto);
        try {
            page = userService.pageUserInfo(dto,pageRequest);
            return super.getResultJSONStr(true, page, "");
        } catch (Exception e) {
            log.error("[user][pageUserInfo] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 删除用户信息
     * @param json
     * @return
     */
    @RequestMapping(value = "/deleteUserInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String deleteUserInfo(@RequestBody String json) {
        Map<String,String> data = new HashMap<>();
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
     * @param json
     * @return
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
     * @param json
     * @return
     */
    @RequestMapping(value = "/findUserInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String findUserInfo(@RequestBody String json) {
        Map<String,String> data = new HashMap<>();
        try {
            UserVO userVO = getFormJSON(json, UserVO.class);
            if (userVO == null || userVO.getId() == null || userVO.getId() == 0L) {
                userVO = new UserVO();
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
     * 查询用户信息id
     * @param json
     * @return
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
            return super.getResultJSONStr(true, users, "");
        } catch (Exception e) {
            log.error("[user][listUserInfo] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 用户信息查询
     */
    @RequestMapping(value = "/findOrgList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String findOrgList(@RequestBody String json){
        try {
            List<UserOrgClientVO> userOrgClientVOS  = userService.findOrgList();
            return super.getResultJSONStr(true, userOrgClientVOS, "");
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
        PageDTO<UserOrgClientVO> page = new PageDTO<>();
        //获取前端用户传来的实名验证相关信息
        UserOrgClientDTO dto = getFormJSON(json,UserOrgClientDTO.class);
        PageRequest pageRequest = genPageRequest(dto);
        try {
            UserOrgClientVO userOrgClientVO = new UserOrgClientVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(dto, userOrgClientVO);
            page = userService.findOrgPage(pageRequest, userOrgClientVO);
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


}
