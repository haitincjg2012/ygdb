package com.apec.systemuser.web;

import com.apec.framework.common.Constants;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.StringUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.systemuser.service.SysRoleResourceService;
import com.apec.systemuser.vo.SysResourceVO;
import com.apec.systemuser.vo.SysRoleResourceVO;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "sysRoleResource")
public class SysRoleResourceController extends MyBaseController
{
    @Autowired
    SysRoleResourceService service;

    @InjectLogger
    private Logger log;

    @RequestMapping(value = "/selectAll", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<List<SysRoleResourceVO>> selectAll(@RequestBody String json)
    {
        ResultData<List<SysRoleResourceVO>> resultData = new ResultData<List<SysRoleResourceVO>>();
        try{
        	log.error(json);
        	String userRoleNo = getUserRoleNo(json);
        	log.error("userRoleNo    " + userRoleNo);
        	if(StringUtil.isNotEmpty(userRoleNo)){
        		SysRoleResourceVO vo = new SysRoleResourceVO();
        		vo.setRid(userRoleNo);
        		List<SysRoleResourceVO> data = service.selectAll(vo);
                resultData.setData(data);
                resultData.setSucceed(true);
        	}else{
        		SysRoleResourceVO dto = getFormJSON(json, SysRoleResourceVO.class);
        		if(StringUtil.isNotEmpty(dto.getRid())){
        			List<SysRoleResourceVO> data = service.selectAll(dto);
        			resultData.setData(data);
        			resultData.setSucceed(true);
        		}else{
        			log.error("selectAll  角色ID为空" );
                    setErrorResultDate(resultData, Constants.SYS_ERROR);
        		}
        	}
        }catch (Exception e){
        	log.error("selectAll", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
        }
        return resultData;
    }
    
    @RequestMapping(value = "/selectAllResource", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<List<SysResourceVO>> selectAllResource(@RequestBody String json)
    {
        ResultData<List<SysResourceVO>> resultData = new ResultData<List<SysResourceVO>>();
        try{
        	SysRoleResourceVO dto = getFormJSON(json, SysRoleResourceVO.class);
            List<SysResourceVO> data = service.selectAllResource(dto);
            resultData.setData(data);
            resultData.setSucceed(true);
        } catch (Exception e){
            log.error("selectAllResource", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
        }
        return resultData;
    }
    
    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultData<SysRoleResourceVO> save(@RequestBody String json)
    {
        ResultData<SysRoleResourceVO> resultData = new ResultData<SysRoleResourceVO>();
        try
        {
        	SysRoleResourceVO dto = getFormJSON(json, SysRoleResourceVO.class);
            if(StringUtil.isEmpty(dto.getReids())|| StringUtil.isEmpty(dto.getRid()))
            {
                setErrorResultDate(resultData, Constants.ERROR_100003);
                return resultData;
            }
            Long userId = getUserId(json);
            service.save(dto, userId + "");
            resultData.setSucceed(true);
        }
        catch (Exception e)
        {
            log.error("save", e);
            setErrorResultDate(resultData, Constants.SYS_ERROR);
        }
        return resultData;
    }
}
