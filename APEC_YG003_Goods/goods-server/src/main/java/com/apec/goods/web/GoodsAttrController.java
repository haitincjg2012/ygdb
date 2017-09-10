package com.apec.goods.web;

import com.apec.framework.common.Constants;
import com.apec.framework.log.InjectLogger;
import com.apec.goods.service.GoodsAttrService;
import com.apec.goods.vo.GoodsAttrVO;
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
 */
@RestController
@RequestMapping("/goodsAttr")
public class GoodsAttrController extends MyBaseController {

    @InjectLogger
    private Logger log;

    @Autowired
    private GoodsAttrService goodsAttrService;

    /**
     * 增加商品属性关系
     */
    @RequestMapping(value = "/saveGoodsAttr",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String saveGoodsAttr(@RequestBody String json){
        String result = Constants.SYS_ERROR;
        try{
            //获取前端数据
            GoodsAttrVO goodsAttrVO = getFormJSON(json,GoodsAttrVO.class);
            if(goodsAttrVO == null || goodsAttrVO.getGoodsId() == null || goodsAttrVO.getGoodsId() == 0L
                    || goodsAttrVO.getAttrId() == null || goodsAttrVO.getAttrId() == 0L
                    || goodsAttrVO.getAttributeShowLevel() == null ){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            result = goodsAttrService.saveGoodsAttr(goodsAttrVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                return super.getResultJSONStr(true, null, "");
            }else{
                return super.getResultJSONStr(false, null, result);
            }
        }catch (Exception e){
            log.error("[goodsAttr][saveGoodsAttr] exception:{}",e);
            return super.getResultJSONStr(false, null, result);
        }
    }

    /**
     * 修改商品属性关系
     */
    @RequestMapping(value = "/updateGoodsAttr",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String updateGoodsAttr(@RequestBody String json){
        String result = Constants.SYS_ERROR;
        try{
            //获取前端数据
            GoodsAttrVO goodsAttrVO = getFormJSON(json,GoodsAttrVO.class);
            if(goodsAttrVO == null || goodsAttrVO.getId() == null ){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            result = goodsAttrService.updateGoodsAttr(goodsAttrVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                return super.getResultJSONStr(true, null, "");
            }else{
                return super.getResultJSONStr(false, null, result);
            }
        }catch (Exception e){
            log.error("[goodsAttr][updateGoodsAttr] exception:{}",e);
            return super.getResultJSONStr(false, null, result);
        }
    }

    /**
     * 删除商品属性关系
     */
    @RequestMapping(value = "/removeGoodsAttr",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String removeGoodsAttr(@RequestBody String json){
        String result = Constants.SYS_ERROR;
        try{
            //获取前端数据
            GoodsAttrVO goodsAttrVO = getFormJSON(json,GoodsAttrVO.class);
            if(goodsAttrVO == null || goodsAttrVO.getId() == null ){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            result = goodsAttrService.removeGoodsAttr(goodsAttrVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                return super.getResultJSONStr(true, null, "");
            }else{
                return super.getResultJSONStr(false, null, result);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("[goodsAttr][removeGoodsAttr] exception:{}",e);
            return super.getResultJSONStr(false, null, result);
        }
    }

    /**
     * 查询商品属性关系
     */
    @RequestMapping(value = "/findGoodsAttr",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String findGoodsAttr(@RequestBody String json){
        try{
            GoodsAttrVO goodsAttrVO1 = new GoodsAttrVO();
            //获取前端数据
            GoodsAttrVO goodsAttrVO = getFormJSON(json,GoodsAttrVO.class);
            if(goodsAttrVO == null || goodsAttrVO.getId() == null ){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            goodsAttrVO1 = goodsAttrService.findGoodsAttr(goodsAttrVO);
            return super.getResultJSONStr(true, goodsAttrVO1, "");
        }catch (Exception e){
            log.error("[goodsAttr][findGoodsAttr] exception:{}",e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 批量删除商品属性信息
     */
    @RequestMapping(value = "/deleteGoodsAttrList",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String deleteGoodsAttrList(@RequestBody String json){
        try{
            ViewVO viewVO = getFormJSON(json, ViewVO.class);
            if(viewVO == null || viewVO.getIds() == null || viewVO.getIds().size() <= 0){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            goodsAttrService.deleteGoodsAttrList(viewVO.getIds(),String.valueOf(getUserId(json)));
            return super.getResultJSONStr(true, null, "");
        }catch(Exception e){
            log.error("[goodsAttr][deleteGoodsAttrList] Exception:{}" , e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }


}
