package com.apec.systemconfig.web;

import com.apec.framework.common.Constants;
import com.apec.framework.common.PageDTO;
import com.apec.framework.log.InjectLogger;
import com.apec.systemconfig.service.RegionLevelService;
import com.apec.systemconfig.vo.RegionLevelVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hmy on 2017/7/28.
 * @author hmy
 */
@RestController
@RequestMapping("/regionLevel")
public class RegionLevelController extends MyBaseController {

    @InjectLogger
    private Logger log;

    @Autowired
    private RegionLevelService regionLevelService;

    @RequestMapping(value = "/listRegionLevel",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String listRegionLevel(@RequestBody String json){
        try {
            RegionLevelVO regionLevelVO = getFormJSON(json,RegionLevelVO.class);
            List<RegionLevelVO> list = regionLevelService.selectAll(regionLevelVO);
            return super.getResultJSONStr(true,list,"");
        }catch (Exception e){
            log.error("[regionLevel]listRegionLevel,{}",e);
            return super.getResultJSONStr(false,null,"");
        }

    }

    @RequestMapping(value = "/listNeedRegionLevel",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String listNeedRegionLevel(@RequestBody String json){
        try {
            List<String> regionCodes = getFormJSON(json,List.class);
            List<RegionLevelVO> list = regionLevelService.getNeedRegionLevel(regionCodes);
            return super.getResultJSONStr(true,list,"");
        }catch (Exception e){
            log.error("[regionLevel]listNeedRegionLevel,{}",e);
            return super.getResultJSONStr(false,null,"");
        }

    }

    @RequestMapping(value = "/pageNeedRegionLevel",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String pageNeedRegionLevel(@RequestBody String json){
        try {
            RegionLevelVO regionLevelVO = getFormJSON(json,RegionLevelVO.class);
            //煤业查询条数设置足够大，50
            regionLevelVO.setPageSize(50);
            PageRequest pageRequest = genPageRequest(regionLevelVO);

            PageDTO<RegionLevelVO> page ;
            PageDTO<RegionLevelVO> pageDTO = null;
            if(regionLevelVO == null || StringUtils.isBlank(regionLevelVO.getCode())){
                //没有传入初始父级地区id，默认查询数据库中最顶级的两个等级的地区，分页形式，如数据库中存储最大等级的地区为省，则查询省市两个等级的分页信息
                page = regionLevelService.pagemaxRegionLevel(pageRequest);
            }else{
                regionLevelVO.setParentId(regionLevelVO.getCode());
                //以传入的地区id为父级查询，如传入的为山东省的id，则查询山东省下的市的分页信息和第一个市的镇区分页信息
                page = regionLevelService.pageRegionLevel(regionLevelVO,pageRequest);
            }
            if(page != null && page.getRows() != null && page.getRows().size() > 0){
                //查询第一页信息
                regionLevelVO.setPageSize(50);
                pageRequest = genPageRequest(regionLevelVO);
                //如果存在第一个地区信息，将获取第一个地区直属管辖的地区信息
                RegionLevelVO vo = page.getRows().get(0);
                regionLevelVO = new RegionLevelVO();
                regionLevelVO.setParentId(vo.getCode());
                pageDTO = regionLevelService.pageRegionLevel(regionLevelVO,pageRequest);
            }
            Map<String,PageDTO<RegionLevelVO>> result = new HashMap<>(16);
            result.put("city",page);
            result.put("subcity",pageDTO);
            return super.getResultJSONStr(true,result,"");
        }catch (Exception e){
            log.error("[regionLevel]pageNeedRegionLevel,{}",e);
            return super.getResultJSONStr(false,null, Constants.SYS_ERROR);
        }

    }

    @RequestMapping(value = "/pageNextRegionLevel",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String pageNextRegionLevel(@RequestBody String json){
        try {
            RegionLevelVO regionLevelVO = getFormJSON(json,RegionLevelVO.class);
            regionLevelVO.setPageSize(50);
            PageRequest pageRequest = genPageRequest(regionLevelVO);

            PageDTO<RegionLevelVO> page ;
            if(regionLevelVO == null || StringUtils.isBlank(regionLevelVO.getCode())){
                //没有传入初始父级地区id，默认查询数据库中最顶级的两个等级的地区，分页形式，如数据库中存储最大等级的地区为省，则查询省市两个等级的分页信息
                return super.getResultJSONStr(false,null,Constants.ERROR_100003);
            }else{
                regionLevelVO.setParentId(regionLevelVO.getCode());
                //以传入的地区id为父级查询，如传入的为山东省的id，则查询山东省下的市的分页信息
                page = regionLevelService.pageRegionLevel(regionLevelVO,pageRequest);
            }
            return super.getResultJSONStr(true,page,"");
        }catch (Exception e){
            log.error("[regionLevel]pageNextRegionLevel,{}",e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }

    }

    /**
     * 查询地址信息（从最低级查到最顶级）
     */
    @RequestMapping(value = "/searchAddress",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String searchAddress(@RequestBody String json){
        try {
            RegionLevelVO regionLevelVO = getFormJSON(json,RegionLevelVO.class);
            if(regionLevelVO == null || StringUtils.isBlank(regionLevelVO.getCode())){
                return super.getResultJSONStr(false,null, Constants.ERROR_100003);
            }
            String address = regionLevelService.getAddressInfo(regionLevelVO.getCode());
            return super.getResultJSONStr(true,address,"");
        }catch (Exception e){
            log.error("[regionLevel]searchAddress,{}",e);
            return super.getResultJSONStr(false,null, Constants.SYS_ERROR);
        }

    }



}
