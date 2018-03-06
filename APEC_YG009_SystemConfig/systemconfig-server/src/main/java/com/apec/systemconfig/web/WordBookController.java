package com.apec.systemconfig.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.apec.framework.common.Constants;
import com.apec.framework.log.InjectLogger;
import com.apec.systemconfig.service.RegionLevelService;
import com.apec.systemconfig.service.WordBookService;
import com.apec.systemconfig.vo.RegionLevelVO;
import com.apec.systemconfig.vo.WordBookViewVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;

/**
 * Created by hmy on 2017/8/3.
 * @author hmy
 */
@RestController
@RequestMapping("/wordBook")
public class WordBookController extends MyBaseController {

    @InjectLogger
    private Logger logger;

    @Autowired
    private WordBookService wordBookService;

    @Autowired
    private RegionLevelService regionLevelService;

    /**
     * 获取需要的字典表相关数据，根据code
     */
    @RequestMapping(value = "/listNeedWordBook",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String listNeedWordBook(@RequestBody String json){
        WordBookViewVO vo = getFormJSON(json,WordBookViewVO.class);
        if(vo == null || StringUtils.isBlank(vo.getCode())){
            return super.getResultJSONStr(false,null, Constants.ERROR_100003);
        }
        try{
            List<WordBookViewVO> list = wordBookService.listNeedWordBook(vo);
            return super.getResultJSONStr(true,list, "");
        }catch(Exception e){
            logger.error("[wordBook][listNeedWordBook]",e);
            return super.getResultJSONStr(false,null, Constants.SYS_ERROR);
        }
    }


    /**
     * 打包字典表中数据，生成文件给前端
     */
    @RequestMapping(value = "/getNeedWordBook",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String getNeedWordBook(@RequestBody String json){
        try{
            JSONArray ja = new JSONArray();
            WordBookViewVO vo = new WordBookViewVO();
            //规格code
            vo.setCode(Constants.FORMAT_CODE);
            //规格数据
            Iterable<WordBookViewVO> formats = wordBookService.listNeedWordBook(vo);
            JSONObject jo = new JSONObject();
            JSONArray jaFormats = new JSONArray();
            Iterator<WordBookViewVO> it = formats.iterator();
            while(it.hasNext()){
                WordBookViewVO viewVO = it.next();
                if(viewVO == null){
                    continue;
                }
                JSONObject joFormat = new JSONObject();
                joFormat.put("keyWord",viewVO.getKeyword());
                jaFormats.add(joFormat);
            }
            jo.put(Constants.FORMAT_CODE,jaFormats);
            ja.add(jo);
            //品种code
            vo.setCode(Constants.VARIETY_CODE);
            //品种数据
            Iterable<WordBookViewVO> variety = wordBookService.listNeedWordBook(vo);
            jo = new JSONObject();
            JSONArray jaVarietys = new JSONArray();
            it = variety.iterator();
            while(it.hasNext()){
                WordBookViewVO viewVO = it.next();
                if(viewVO == null){
                    continue;
                }
                JSONObject joVariety = new JSONObject();
                joVariety.put("keyWord",viewVO.getKeyword());
                jaVarietys.add(joVariety);
            }
            jo.put(Constants.VARIETY_CODE,jaVarietys);
            ja.add(jo);
            //区域code
            vo.setCode(Constants.AREA_CODE);
            //品种数据
            Iterable<WordBookViewVO> areas = wordBookService.listNeedWordBook(vo);
            jo = new JSONObject();
            JSONArray jaAreas = new JSONArray();
            it = areas.iterator();
            while(it.hasNext()){
                WordBookViewVO viewVO = it.next();
                if(viewVO == null){
                    continue;
                }
                JSONObject joArea = new JSONObject();
                joArea.put("keyWord",viewVO.getKeyword());
                joArea.put("value",viewVO.getValue());
                RegionLevelVO regionLevelVO = new RegionLevelVO();
                JSONArray jaSubCity = new JSONArray();
                if(StringUtils.isNotBlank(viewVO.getValue())){
                    regionLevelVO.setParentId(viewVO.getValue());
                    //获取配置区域的下属管辖区域
                    List<RegionLevelVO> regionLevelVOS = regionLevelService.selectAll(regionLevelVO);

                    for(RegionLevelVO vo1:regionLevelVOS){
                        JSONObject joSubCity = new JSONObject();
                        joSubCity.put("keyWord",vo1.getName());
                        jaSubCity.add(joSubCity);
                    }
                }
                joArea.put(viewVO.getValue(),jaSubCity);
                jaAreas.add(joArea);
            }
            jo.put(Constants.AREA_CODE,jaAreas);
            ja.add(jo);

            return super.getResultJSONStr(true,ja, "");
        }catch(Exception e){
            logger.error("[wordBook][getNeedWordBook]",e);
            return super.getResultJSONStr(false,null, Constants.SYS_ERROR);
        }
    }


}
