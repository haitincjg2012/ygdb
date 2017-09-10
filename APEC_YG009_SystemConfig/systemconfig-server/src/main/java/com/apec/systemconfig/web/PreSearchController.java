package com.apec.systemconfig.web;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.SearchType;
import com.apec.framework.common.util.JsonUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.systemconfig.service.PreSearchService;
import com.apec.systemconfig.vo.PreSearchVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hmy on 2017/8/1.
 */
@RestController
@RequestMapping(value = "/preSearch")
public class PreSearchController extends MyBaseController {

    @InjectLogger
    private Logger log;

    @Autowired
    private PreSearchService preSearchService;

    @RequestMapping(value = "/getPreSearchInfo",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String getPreSearchInfo(@RequestBody String json){
        PreSearchVO preSearchVO = getFormJSON(json,PreSearchVO.class);
        try{
            if(preSearchVO != null && preSearchVO.getSearchType() != null && StringUtils.equals(preSearchVO.getSearchType().name(), SearchType.YZSS.name())){
                PreSearchVO yzPreSearch = preSearchService.getYZPreSearch();
                return super.getResultJSONStr(true,yzPreSearch,"");
            }else{
                //不是查询预制类型，则查询热门类型的数据
                List<PreSearchVO> rmPreSearch = preSearchService.listRMPreSearch();
                return super.getResultJSONStr(true,rmPreSearch,"");
            }
        }catch(Exception e){
            log.error("[preSearch]getPreSearchInfo:{}",e);
            return super.getResultJSONStr(false,null, Constants.SYS_ERROR);
        }

    }


}
