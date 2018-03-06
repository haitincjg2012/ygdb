package com.apec.goods.web;

import com.apec.framework.common.Constants;
import com.apec.framework.log.InjectLogger;
import com.apec.goods.service.AttributeValueService;
import com.apec.goods.vo.AttributeValueVO;
import com.apec.goods.vo.ViewVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hmy on 2017/7/11.
 * @author hmy
 */
@RestController
@RequestMapping("/attributeValue")
public class AttributeValueController extends MyBaseController {

    @InjectLogger
    private Logger log;

    @Autowired
    private AttributeValueService attributeValueService;

    /**
     * 增加属性值对象
     */
    @RequestMapping(value = "/saveAttributeValue" ,method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String saveAttributeValue(@RequestBody String json){
        String result = Constants.SYS_ERROR;
        try{
            //获取商品信息
            AttributeValueVO attributeValueVO = getFormJSON(json,AttributeValueVO.class);
            if(attributeValueVO == null || StringUtils.isEmpty(attributeValueVO.getAttrValue()) || attributeValueVO.getAttributeNameId() == null){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            result = attributeValueService.saveAttributeValue(attributeValueVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                return super.getResultJSONStr(true, null, "");
            }else{
                return super.getResultJSONStr(false, null, result);
            }
        }catch(Exception e){
            log.error("[attributeValue][saveAttributeValue] Exception:{}" , e);
            return super.getResultJSONStr(false, null, result);
        }
    }

    /**
     * 修改属性值对象
     */
    @RequestMapping(value = "/updateAttributeValue" ,method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String updateAttributeValue(@RequestBody String json){
        String result = Constants.SYS_ERROR;
        try{
            //获取商品信息
            AttributeValueVO attributeValueVO = getFormJSON(json,AttributeValueVO.class);
            if(attributeValueVO == null || attributeValueVO.getId() == null || StringUtils.isBlank(attributeValueVO.getAttrValue())){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            result = attributeValueService.updateAttributeValue(attributeValueVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                return super.getResultJSONStr(true, null, "");
            }else{
                return super.getResultJSONStr(false, null, result);
            }
        }catch(Exception e){
            log.error("[attributeValue][updateAttributeValue] Exception:{}" , e);
            return super.getResultJSONStr(false, null, result);
        }
    }

    /**
     * 删除属性值对象
     */
    @RequestMapping(value = "/removeAttributeValue" ,method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String removeAttributeValue(@RequestBody String json){
        String result = Constants.SYS_ERROR;
        try{
            //获取商品信息
            AttributeValueVO attributeValueVO = getFormJSON(json,AttributeValueVO.class);
            if(attributeValueVO == null || attributeValueVO.getId() == null){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            result = attributeValueService.removeAttributeValue(attributeValueVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                return super.getResultJSONStr(true, null, "");
            }else{
                return super.getResultJSONStr(false, null, result);
            }
        }catch(Exception e){
            log.error("[attributeValue][removeAttributeValue] Exception:{}" , e);
            return super.getResultJSONStr(false, null, result);
        }
    }

    /**
     * 查询属性值对象
     */
    @RequestMapping(value = "/findAttributeValue" ,method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String findAttributeValue(@RequestBody String json){
        try{
            //获取商品信息
            AttributeValueVO attributeValueVO = getFormJSON(json,AttributeValueVO.class);
            if(attributeValueVO == null || attributeValueVO.getId() == null){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            AttributeValueVO attributeValueVO1 = attributeValueService.findAttributeValue(attributeValueVO);
            return super.getResultJSONStr(true, attributeValueVO1, "");
        }catch(Exception e){
            e.printStackTrace();
            log.error("[attributeValue][findAttributeValue] Exception:{}" , e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 批量删除属性名称信息
     */
    @RequestMapping(value = "/deleteAttributeValueList",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String deleteAttributeValueList(@RequestBody String json){
        try{
            ViewVO viewVO = getFormJSON(json, ViewVO.class);
            if(viewVO == null || viewVO.getIds() == null || viewVO.getIds().size() <= 0){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            attributeValueService.deleteAttributeValueList(viewVO.getIds(),String.valueOf(getUserId(json)));
            return super.getResultJSONStr(true, null, "");
        }catch(Exception e){
            log.error("[attributeValue][deleteAttributeValueList] Exception:{}" , e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }




}
