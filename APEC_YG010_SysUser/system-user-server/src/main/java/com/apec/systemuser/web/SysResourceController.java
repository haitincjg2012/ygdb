package com.apec.systemuser.web;

import com.apec.framework.common.Constants;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.StringUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.systemuser.service.SysResourceService;
import com.apec.systemuser.vo.SysResourceVO;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xxxx
 */
@RestController
@RequestMapping(value = "sysResourc")
public class SysResourceController extends MyBaseController
{
    @Autowired
    SysResourceService service;

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
     * @return 结果
     */
    @RequestMapping(value = "searchList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<PageDTO<SysResourceVO>> searchList(@RequestBody String json)
    {
        ResultData<PageDTO<SysResourceVO>> resultData = new ResultData<>();
        try
        {
            SysResourceVO dto = getFormJSON(json, SysResourceVO.class);
            PageRequest pageRequest = genPageRequest(dto);
            PageDTO<SysResourceVO> data = service.searchPage(dto, pageRequest);
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
     */
    @RequestMapping(value = "/selectAll", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<List<SysResourceVO>> selectAll(@RequestBody String json)
    {
        ResultData<List<SysResourceVO>> resultData = new ResultData<>();
        try
        {
            SysResourceVO dto = getFormJSON(json, SysResourceVO.class);
            List<SysResourceVO> data = service.selectAll(dto);
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
     * 资源信息详情
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<SysResourceVO> detail(@RequestBody String json)
    {
        ResultData<SysResourceVO> resultData = new ResultData<>();
        try
        {
            SysResourceVO dto = getFormJSON(json, SysResourceVO.class);
            if(dto.getId() == null)
            {
                setErrorResultDate(resultData, Constants.ERROR_100003);
                return resultData;
            }
            Long id = dto.getId();
            SysResourceVO data = service.findOne(id);
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
     * 新增资源
     * @param json
     * {
     *      orgCode,
     *      name,
     *      mobile,
     *      loginName,
     *      password
     * }
     * @return 结果
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<SysResourceVO> add(@RequestBody String json)
    {
        ResultData<SysResourceVO> resultData = new ResultData<>();
        try
        {
            SysResourceVO dto = getFormJSON(json, SysResourceVO.class);
            if(StringUtil.isEmpty(dto.getName()) || StringUtil.isEmpty(dto.getType())
               || StringUtil.isEmpty(dto.getResource()))
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
     * 更新资源信息
     * @param json
     * {
     *      id
     * }
     * @return 结果
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<SysResourceVO> update(@RequestBody String json)
    {
        ResultData<SysResourceVO> resultData = new ResultData<>();
        try
        {
            SysResourceVO dto = getFormJSON(json, SysResourceVO.class);
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
     * 软删除资源信息
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<SysResourceVO> delete(@RequestBody String json)
    {
        ResultData<SysResourceVO> resultData = new ResultData<>();
        try
        {
            SysResourceVO dto = getFormJSON(json, SysResourceVO.class);
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
