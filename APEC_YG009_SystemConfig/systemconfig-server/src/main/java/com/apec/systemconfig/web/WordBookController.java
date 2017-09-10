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
     * @param json
     * @return
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
     * @param json
     * @return
     */
    @RequestMapping(value = "/getNeedWordBook",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String getNeedWordBook(@RequestBody String json){
        try{
            JSONArray ja = new JSONArray();
            WordBookViewVO vo = new WordBookViewVO();
            vo.setCode(Constants.FORMAT_CODE);//规格code
            Iterable<WordBookViewVO> formats = wordBookService.listNeedWordBook(vo);//规格数据
            JSONObject jo = new JSONObject();
            JSONArray ja_formats = new JSONArray();
            Iterator<WordBookViewVO> it = formats.iterator();
            while(it.hasNext()){
                WordBookViewVO viewVO = it.next();
                if(viewVO == null){
                    continue;
                }
                JSONObject jo_format = new JSONObject();
                jo_format.put("keyWord",viewVO.getKeyword());
                ja_formats.add(jo_format);
            }
            jo.put(Constants.FORMAT_CODE,ja_formats);
            ja.add(jo);
            vo.setCode(Constants.VARIETY_CODE);//品种code
            Iterable<WordBookViewVO> variety = wordBookService.listNeedWordBook(vo);//品种数据
            jo = new JSONObject();
            JSONArray ja_varietys = new JSONArray();
            it = variety.iterator();
            while(it.hasNext()){
                WordBookViewVO viewVO = it.next();
                if(viewVO == null){
                    continue;
                }
                JSONObject jo_variety = new JSONObject();
                jo_variety.put("keyWord",viewVO.getKeyword());
                ja_varietys.add(jo_variety);
            }
            jo.put(Constants.VARIETY_CODE,ja_varietys);
            ja.add(jo);
            vo.setCode(Constants.AREA_CODE);//区域code
            Iterable<WordBookViewVO> areas = wordBookService.listNeedWordBook(vo);//品种数据
            jo = new JSONObject();
            JSONArray ja_areas = new JSONArray();
            it = areas.iterator();
            while(it.hasNext()){
                WordBookViewVO viewVO = it.next();
                if(viewVO == null){
                    continue;
                }
                JSONObject jo_area = new JSONObject();
                jo_area.put("keyWord",viewVO.getKeyword());
                jo_area.put("value",viewVO.getValue());
                RegionLevelVO regionLevelVO = new RegionLevelVO();
                JSONArray ja_subCity = new JSONArray();
                if(StringUtils.isNotBlank(viewVO.getValue())){
                    regionLevelVO.setParentId(viewVO.getValue());
                    //获取配置区域的下属管辖区域
                    List<RegionLevelVO> regionLevelVOS = regionLevelService.selectAll(regionLevelVO);

                    for(RegionLevelVO vo1:regionLevelVOS){
                        JSONObject jo_subCity = new JSONObject();
                        jo_subCity.put("keyWord",vo1.getName());
                        ja_subCity.add(jo_subCity);
                    }
                }
                jo_area.put(viewVO.getValue(),ja_subCity);
                ja_areas.add(jo_area);
            }
            jo.put(Constants.AREA_CODE,ja_areas);
            ja.add(jo);

            return super.getResultJSONStr(true,ja, "");
        }catch(Exception e){
            logger.error("[wordBook][getNeedWordBook]",e);
            return super.getResultJSONStr(false,null, Constants.SYS_ERROR);
        }
    }


}
