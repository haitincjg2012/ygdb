package com.apec.systemuser.web;

import com.apec.framework.common.Constants;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.StringUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.springcloud.SpringCloudClient;
import com.apec.systemuser.service.SysUserService;
import com.apec.systemuser.vo.SysUserVO;
import com.apec.systemuser.vo.SysUserViewVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "sysUser")
public class SysUserController extends MyBaseController
{
    @Autowired
    SysUserService service;
    @Autowired
    private SpringCloudClient springCloudClient;
    @InjectLogger
    private Logger log;

    /**
     * 多种情况列表分页查询（条件可为空）
     * @param json
     * {
     *      (like)name，
     *      (eq)mobile，
     *      (eq)orgCode，
     *      (like)loginName
     * }
     * @return
     */
    @RequestMapping(value = "searchList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<PageDTO<SysUserVO>> searchList(@RequestBody String json)
    {
        ResultData<PageDTO<SysUserVO>> resultData = new ResultData<PageDTO<SysUserVO>>();
        try
        {
            SysUserVO dto = getFormJSON(json, SysUserVO.class);
            PageRequest pageRequest = genPageRequest(dto);
            PageDTO<SysUserVO> data = service.searchPage(dto, pageRequest);
            resultData.setData(data);
            resultData.setSucceed(true);
        }
        catch (Exception e)
        {
            log.error("sysUser/searchList", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
        }
        return resultData;
    }


    /**
     * 根据条件查询
     * @param json
     * @return
     */
    @RequestMapping(value = "/queryUserNameById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<String> queryUserNameById(@RequestBody String json)
    {
        ResultData<String> resultData = new ResultData<String>();
        try
        {
            SysUserVO dto = getFormJSON(json, SysUserVO.class);
            resultData.setData(service.querySysUserById(dto.getId()));
            resultData.setSucceed(true);
        }
        catch (Exception e)
        {
            log.error("sysUser/queryUserNameById", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
        }
        return resultData;
    }
    /**
     * 根据条件查询
     * @param json
     * @return
     */
    @RequestMapping(value = "/queryUserNameByIds", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<List<SysUserViewVO>> queryUserNameByIds(@RequestBody String json)
    {
        ResultData<List<SysUserViewVO>> resultData = new ResultData<>();
        try
        {
            SysUserViewVO dto = getFormJSON(json, SysUserViewVO.class);
            if(StringUtils.isBlank(dto.getId())){
                log.error("sysUser/queryUserNameByIds, id is null");
                setErrorResultDate(resultData, Constants.DATA_ISNULL);
            }
            List<String> list = Arrays.asList(dto.getId().split(","));
            List<Long> listIds = new ArrayList<>();
            list.forEach(obj ->{
                listIds.add(Long.parseLong(obj));
            });

            resultData.setData(service.listSysUserByIds(listIds));
            resultData.setSucceed(true);
        }
        catch (Exception e)
        {
            log.error("sysUser/queryUserNameByIds", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
        }
        return resultData;
    }

    /**
     * 根据条件查询
     * @param json
     * @return
     */
    @RequestMapping(value = "/selectAll", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<List<SysUserVO>> selectAll(@RequestBody String json)
    {
        ResultData<List<SysUserVO>> resultData = new ResultData<List<SysUserVO>>();
        try
        {
            SysUserVO dto = getFormJSON(json, SysUserVO.class);
//            String orgCode = getOrgCode(json);
//            if(StringUtil.isNotEmpty(orgCode)){
//            	dto.setOrgCode(getOrgCode(json));
//            }
            List<SysUserVO> data = service.selectAll(dto);
            resultData.setData(data);
            resultData.setSucceed(true);
        }
        catch (Exception e)
        {
            log.error("sysUser/selectAll", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
        }
        return resultData;
    }

    /**
     * 用户信息详情
     * @param json
     * {
     *      id
     * }
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<SysUserVO> detail(@RequestBody String json)
    {
        ResultData<SysUserVO> resultData = new ResultData<SysUserVO>();
        try
        {
            SysUserVO dto = getFormJSON(json, SysUserVO.class);
            if(dto.getId() == null)
            {
                setErrorResultDate(resultData, Constants.ERROR_100003);
                return resultData;
            }
            Long id = dto.getId();
            SysUserVO data = service.findOne(id);
            resultData.setData(data);
            resultData.setSucceed(true);
        }
        catch (Exception e)
        {
            log.error("SysUser/detail", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
            return resultData;
        }
        return resultData;
    }

    /**
     * 新增用户
     * @param json
     * {
     *      orgCode,
     *      name,
     *      mobile,
     *      loginName,
     *      password
     * }
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<SysUserVO> add(@RequestBody String json)
    {
        ResultData<SysUserVO> resultData = new ResultData<SysUserVO>();
        try
        {
            SysUserVO dto = getFormJSON(json, SysUserVO.class);
			if (StringUtil.isEmpty(dto.getName()) || StringUtil.isEmpty(dto.getPassword())
					|| StringUtil.isEmpty(dto.getMobile()))
            {
                setErrorResultDate(resultData, Constants.ERROR_100003);
                return resultData;
            }
//            SysUserVO tem = new SysUserVO();
//            tem.setLoginName(dto.getLoginName());
//            boolean exist = service.isExist(tem);
//            if(exist)
//            {
//                setErrorResultDate(resultData, Constants.COMMON_IS_EXIST);
//                return resultData;
//            }
            SysUserVO tem1 = new SysUserVO();
            tem1.setMobile(dto.getMobile());
            boolean exist = service.isExist(tem1);
            if(exist)
            {
                setErrorResultDate(resultData, Constants.COMMON_IS_EXIST);
                return resultData;
            }
            Long userId = getUserId(json);
            service.insert(dto, userId + "");
            resultData.setSucceed(true);
        }
        catch (Exception e)
        {
            log.error("sysUser/add", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
        }
        return resultData;
    }

    /**
     * 更新用户信息
     * @param json
     * {
     *      id
     * }
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<SysUserVO> update(@RequestBody String json)
    {
        ResultData<SysUserVO> resultData = new ResultData<SysUserVO>();
        try
        {
            SysUserVO dto = getFormJSON(json, SysUserVO.class);
            if(dto.getId() == null)
            {
                setErrorResultDate(resultData, Constants.ERROR_100003);
                return resultData;
            }
            Long userId = getUserId(json);
            service.update(dto, userId + "");
            resultData.setSucceed(true);
        }
        catch (Exception e)
        {
            log.error("sysUser/update", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
        }
        return resultData;
    }

    /**
     * 软删除用户信息
     * @param json
     * {
     *      id
     * }
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<SysUserVO> delete(@RequestBody String json)
    {
        ResultData<SysUserVO> resultData = new ResultData<SysUserVO>();
        try
        {
            SysUserVO dto = getFormJSON(json, SysUserVO.class);
            if(dto.getId() == null)
            {
                setErrorResultDate(resultData, Constants.ERROR_100003);
                return resultData;
            }
            Long userId = getUserId(json);
            Long id = dto.getId();
            service.delete(id, userId);
            resultData.setSucceed(true);
        }
        catch (Exception e)
        {
            log.error("sysUser/delete", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
        }
        return resultData;
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<SysUserVO> login(@RequestBody String json)
    {
        ResultData<SysUserVO> resultData = new ResultData<SysUserVO>();
        try
        {
            SysUserVO dto = getFormJSON(json, SysUserVO.class);
            if(StringUtil.isEmpty(dto.getLoginName())|| StringUtil.isEmpty(dto.getPassword())){
                setErrorResultDate(resultData, Constants.ERROR_100003);
                return resultData;
            }
//            SysUserVO vo = new SysUserVO();
//            vo.setLoginName(dto.getLoginName());
//            List<SysUserVO> list = service.selectAll(vo);
            SysUserVO vo1 = new SysUserVO();
            vo1.setMobile(dto.getLoginName());
            List<SysUserVO> list = service.selectAll(vo1);
            if(null==list||list.size()==0){
            	setErrorResultDate(resultData, Constants.USER_NOT_EXIST_ERROR);
                return resultData;
            }
            SysUserVO sysUser = new SysUserVO();
            sysUser = list.get(0);
            if(!dto.getPassword().equals(sysUser.getPassword())){
            	setErrorResultDate(resultData, Constants.PASSWORD_ERROR);
                return resultData;
            }
            sysUser.setPassword("");
            resultData.setData(sysUser);
            resultData.setSucceed(true);
        }catch (Exception e){
            log.error("SysUser/login", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
            return resultData;
        }
        return resultData;
    }
    
    @RequestMapping(value = "/changePW", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<SysUserVO> changePW(@RequestBody String json)
    {
        ResultData<SysUserVO> resultData = new ResultData<SysUserVO>();
        try
        {
            SysUserVO dto = getFormJSON(json, SysUserVO.class);
            if(StringUtil.isEmpty(dto.getNewPW()) || StringUtil.isEmpty(dto.getPassword())){
                setErrorResultDate(resultData, Constants.ERROR_100003);
                return resultData;
            }
            Long userId = getUserId(json);
            SysUserVO tem = service.findOne(userId);
            if(dto.getPassword().equals(tem.getPassword())){
            	dto.setId(tem.getId());
            	dto.setPassword(dto.getNewPW());
            	service.update(dto, userId + "");
            	resultData.setSucceed(true);
            }else{
            	setErrorResultDate(resultData, Constants.PASSWORD_ERROR);
            }
        }
        catch (Exception e)
        {
            log.error("sysUser/update", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
        }
        return resultData;
    }
    
    /**
     * 新增用户
     * @param json
     * {
     *      orgCode,
     *      name,
     *      mobile,
     *      loginName,
     *      password
     * }
     * @return
     */
    @RequestMapping(value = "/addForCus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<SysUserVO> addForCus(@RequestBody String json)
    {
        ResultData<SysUserVO> resultData = new ResultData<SysUserVO>();
        try
        {
            SysUserVO dto = getFormJSON(json, SysUserVO.class);
			if (StringUtil.isEmpty(dto.getName()) || StringUtil.isEmpty(dto.getLoginName())
					|| StringUtil.isEmpty(dto.getPassword()) || StringUtil.isEmpty(dto.getMobile())) {
				setErrorResultDate(resultData, Constants.ERROR_100003);
				return resultData;
			}
            SysUserVO tem = new SysUserVO();
            tem.setLoginName(dto.getLoginName());
            boolean exist = service.isExist(tem);
            if(exist)
            {
                setErrorResultDate(resultData, Constants.COMMON_ERROR_PARAMS);
                return resultData;
            }
            SysUserVO tem1 = new SysUserVO();
            tem1.setMobile(dto.getMobile());
            exist = service.isExist(tem1);
            if(exist)
            {
                setErrorResultDate(resultData, Constants.COMMON_ERROR_PARAMS);
                return resultData;
            }
            Long userId = getUserId(json);
            service.insert(dto, userId + "");
            resultData.setSucceed(true);
        }
        catch (Exception e)
        {
            log.error("sysUser/add", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
        }
        return resultData;
    }
}
