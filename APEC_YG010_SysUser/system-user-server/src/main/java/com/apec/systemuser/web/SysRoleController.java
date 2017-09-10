package com.apec.systemuser.web;

import com.apec.framework.common.Constants;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.StringUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.systemuser.service.SysRoleService;
import com.apec.systemuser.vo.SysRoleVO;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "sysRole")
public class SysRoleController extends MyBaseController
{
    @Autowired
    SysRoleService service;

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
    public ResultData<PageDTO<SysRoleVO>> searchList(@RequestBody String json)
    {
        ResultData<PageDTO<SysRoleVO>> resultData = new ResultData<PageDTO<SysRoleVO>>();
        try
        {
            SysRoleVO dto = getFormJSON(json, SysRoleVO.class);
            PageRequest pageRequest = genPageRequest(dto);
            PageDTO<SysRoleVO> data = service.searchPage(dto, pageRequest);
            resultData.setData(data);
            resultData.setSucceed(true);
        }
        catch (Exception e)
        {
            log.error("searchList", e);
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
    public ResultData<List<SysRoleVO>> selectAll(@RequestBody String json)
    {
        ResultData<List<SysRoleVO>> resultData = new ResultData<List<SysRoleVO>>();
        try
        {
            SysRoleVO dto = getFormJSON(json, SysRoleVO.class);
            List<SysRoleVO> data = service.selectAll(dto);
            resultData.setData(data);
            resultData.setSucceed(true);
        }
        catch (Exception e)
        {
            log.error("selectAll", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
        }
        return resultData;
    }

    /**
     * 角色信息详情
     * @param json
     * {
     *      id
     * }
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<SysRoleVO> detail(@RequestBody String json)
    {
        ResultData<SysRoleVO> resultData = new ResultData<SysRoleVO>();
        try
        {
            SysRoleVO dto = getFormJSON(json, SysRoleVO.class);
            if(dto.getId() == null)
            {
                setErrorResultDate(resultData, Constants.ERROR_100003);
                return resultData;
            }
            Long id = dto.getId();
            SysRoleVO data = service.findOne(id);
            resultData.setData(data);
            resultData.setSucceed(true);
        }
        catch (Exception e)
        {
            log.error("detail", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
            return resultData;
        }
        return resultData;
    }

    /**
     * 新增角色
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
    public ResultData<SysRoleVO> add(@RequestBody String json)
    {
        ResultData<SysRoleVO> resultData = new ResultData<SysRoleVO>();
        try
        {
            SysRoleVO dto = getFormJSON(json, SysRoleVO.class);
            if(StringUtil.isEmpty(dto.getName()))
            {
                setErrorResultDate(resultData, Constants.ERROR_100003);
                return resultData;
            }
            Long userId = getUserId(json);
            service.insert(dto, userId + "");
            resultData.setSucceed(true);
        }
        catch (Exception e)
        {
            log.error("add", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
        }
        return resultData;
    }

    /**
     * 更新角色信息
     * @param json
     * {
     *      id
     * }
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<SysRoleVO> update(@RequestBody String json)
    {
        ResultData<SysRoleVO> resultData = new ResultData<SysRoleVO>();
        try
        {
            SysRoleVO dto = getFormJSON(json, SysRoleVO.class);
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
            log.error("update", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
        }
        return resultData;
    }

    /**
     * 软删除角色信息
     * @param json
     * {
     *      id
     * }
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<SysRoleVO> delete(@RequestBody String json)
    {
        ResultData<SysRoleVO> resultData = new ResultData<SysRoleVO>();
        try
        {
            SysRoleVO dto = getFormJSON(json, SysRoleVO.class);
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
            log.error("delete", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
        }
        return resultData;
    }
}
