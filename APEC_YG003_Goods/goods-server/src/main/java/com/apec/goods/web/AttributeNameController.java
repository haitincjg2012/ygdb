package com.apec.goods.web;

import com.apec.framework.common.Constants;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.goods.dto.AttributeNameDTO;
import com.apec.goods.service.AttributeNameService;
import com.apec.goods.vo.AttributeNameVO;
import com.apec.goods.vo.ViewVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hmy on 2017/7/10.
 */
@RestController
@RequestMapping("/attributeName")
public class AttributeNameController extends MyBaseController {

    @InjectLogger
    private Logger log;

    @Autowired
    private AttributeNameService attributeNameService;

    /**
     * 增加属性名称对象
     */
    @RequestMapping(value = "/saveAttributeName",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String saveAttributeName(@RequestBody String json){
        String result = Constants.SYS_ERROR;
        try{
            //获取前端数据
            AttributeNameVO attributeNameVO = getFormJSON(json,AttributeNameVO.class);
            if(attributeNameVO == null || StringUtils.isEmpty(attributeNameVO.getAttrName())){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            result = attributeNameService.saveAttributeName(attributeNameVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                return super.getResultJSONStr(true, null, "");
            }else{
                return super.getResultJSONStr(false, null, result);
            }
        }catch (Exception e){
            log.error("[attributeName][saveAttributeName] exception:{}",e);
            return super.getResultJSONStr(false, null, result);
        }
    }

    /**
     * 修改属性名称对象
     */
    @RequestMapping(value = "/updateAttributeName",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String updateAttributeName(@RequestBody String json){
        String result = Constants.SYS_ERROR;
        try{
            //获取前端数据
            AttributeNameVO attributeNameVO = getFormJSON(json,AttributeNameVO.class);
            if(attributeNameVO == null || attributeNameVO.getId() == null){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            result = attributeNameService.updateAttributeName(attributeNameVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                return super.getResultJSONStr(true, null, "");
            }else{
                return super.getResultJSONStr(false, null, result);
            }
        }catch (Exception e){
            log.error("[attributeName][updateAttributeName] exception:{}",e);
            return super.getResultJSONStr(false, null, result);
        }
    }

    /**
     * 删除属性名称对象
     */
    @RequestMapping(value = "/removeAttributeName",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String removeAttributeName(@RequestBody String json){
        String result = Constants.SYS_ERROR;
        try{
            //获取前端数据
            AttributeNameVO attributeNameVO = getFormJSON(json,AttributeNameVO.class);
            if(attributeNameVO == null || attributeNameVO.getId() == null){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            result = attributeNameService.removeAttributeName(attributeNameVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                return super.getResultJSONStr(true, null, "");
            }else{
                return super.getResultJSONStr(false, null, result);
            }
        }catch (Exception e){
            log.error("[attributeName][removeAttributeName] exception:{}",e);
            return super.getResultJSONStr(false, null, result);
        }
    }

    /**
     * 查询属性名称对象
     */
    @RequestMapping(value = "/findAttributeName",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String findAttributeName(@RequestBody String json){
        try{
            AttributeNameVO attributeNameVO1 = new AttributeNameVO();
            //获取前端数据
            AttributeNameVO attributeNameVO = getFormJSON(json,AttributeNameVO.class);
            if(attributeNameVO == null || attributeNameVO.getId() == null){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            attributeNameVO1 = attributeNameService.findAttributeName(attributeNameVO);
            return super.getResultJSONStr(true, attributeNameVO1, "");
        }catch (Exception e){
            log.error("[attributeName][findAttributeName] exception:{}",e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 获取分页属性信息
     */
    @RequestMapping(value = "/getAttributeNamePage",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String getAttributeNamePage(@RequestBody String json){
        try{
            PageDTO<AttributeNameVO> page = new PageDTO<>();
            AttributeNameDTO dto = getFormJSON(json,AttributeNameDTO.class);
            PageRequest pageRequest = genPageRequest(dto);
            AttributeNameVO attributeNameVO = new AttributeNameVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(dto,attributeNameVO);
            page = attributeNameService.searchAttributeNamePage(attributeNameVO,pageRequest);
            return super.getResultJSONStr(true, page, "");
        }catch(Exception e){
            log.error("[attributeName][getAttributeNamePage] Exception:{}" , e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 批量删除属性名称信息
     */
    @RequestMapping(value = "/deleteAttributeNameList",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String deleteAttributeNameList(@RequestBody String json){
        try{
            ViewVO viewVO = getFormJSON(json, ViewVO.class);
            if(viewVO == null || viewVO.getIds() == null || viewVO.getIds().size() <= 0){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            attributeNameService.deleteAttributeNameList(viewVO.getIds(),String.valueOf(getUserId(json)));
            return super.getResultJSONStr(true, null, "");
        }catch(Exception e){
            log.error("[attributeName][deleteAttributeNameList] Exception:{}" , e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

}
