package com.apec.goods.web;

import com.apec.framework.common.Constants;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.util.BeanUtil;
import com.apec.goods.dto.GoodsDTO;
import com.apec.goods.service.GoodsService;
import com.apec.goods.vo.GoodsVO;
import com.apec.goods.vo.ViewVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import com.apec.framework.log.InjectLogger;


/**
 * 商品模块接口
 *
 * @author yirder
 */
@RestController
@RequestMapping("/goods")
public class GoodsController extends MyBaseController {

    @InjectLogger
   private Logger log;

    @Autowired
    private GoodsService goodsService;

    /**
     * 增加商品
     */
    @RequestMapping(value = "/addGoods" ,method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String addGoods(@RequestBody String json){
        String result = Constants.SYS_ERROR;
        try{
            //获取商品信息
            GoodsVO goodsVO = getFormJSON(json,GoodsVO.class);
            if(goodsVO == null || StringUtils.isEmpty(goodsVO.getGoodsName())){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            result = goodsService.saveGoods(goodsVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                return super.getResultJSONStr(true, null, "");
            }else{
                return super.getResultJSONStr(false, null, result);
            }
        }catch(Exception e){
            log.error("[goods][addGoods] Exception:{}" , e);
            return super.getResultJSONStr(false, null, result);
        }
    }

    /**
     * 修改商品信息
     */
    @RequestMapping(value = "/updateGoods" ,method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String updateGoods(@RequestBody String json){
        String result = Constants.SYS_ERROR;
        try{
            //获取商品信息
            GoodsVO goodsVO = getFormJSON(json,GoodsVO.class);
            if(goodsVO == null || goodsVO.getId() == null){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            result = goodsService.updateGoods(goodsVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                return super.getResultJSONStr(true, null, "");
            }else{
                return super.getResultJSONStr(false, null, result);
            }
        }catch(Exception e){
            log.error("[goods][updateGoods] Exception:{}" , e);
            return super.getResultJSONStr(false, null, result);
        }
    }

    /**
     * 删除商品信息
     */
    @RequestMapping(value = "/removeGoods" ,method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String removeGoods(@RequestBody String json){
        String result = Constants.SYS_ERROR;
        try{
            //获取商品信息
            GoodsVO goodsVO = getFormJSON(json,GoodsVO.class);
            if(goodsVO == null || goodsVO.getId() == null){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            result = goodsService.removeGoods(goodsVO,String.valueOf(getUserId(json)));
            if(StringUtils.equals(result,Constants.RETURN_SUCESS)){
                return super.getResultJSONStr(true, null, "");
            }else{
                return super.getResultJSONStr(false, null, result);
            }
        }catch(Exception e){
            log.error("[goods][removeGoods] Exception:{}" , e);
            return super.getResultJSONStr(false, null, result);
        }
    }

    /**
     * 获取商品具体信息
     */
    @RequestMapping(value = "/getGoods" ,method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String getGoods(@RequestBody String json){
        try{
            //获取商品信息
            GoodsVO goodsVO = getFormJSON(json,GoodsVO.class);
            GoodsVO goodsVO1 = goodsService.getGoods(goodsVO);
            return super.getResultJSONStr(true, goodsVO1, null);
        }catch(Exception e){
            log.error("[goods][getGoods] Exception:{}" , e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 获取分页商品信息
     */
    @RequestMapping(value = "/getGoodsPage",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String getGoodsPage(@RequestBody String json){
        try{
            GoodsDTO dto = getFormJSON(json,GoodsDTO.class);
            PageRequest pageRequest = genPageRequest(dto);
            //获取商品信息
            GoodsVO goodsVO = new GoodsVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(dto,goodsVO);
            PageDTO<GoodsVO> page = goodsService.searchGoodsPage(goodsVO,pageRequest);
            return super.getResultJSONStr(true, page, "");
        }catch(Exception e){
            log.error("[goods][getGoodsPage] Exception:{}" , e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 批量删除商品信息
     */
    @RequestMapping(value = "/deleteGoodsList",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String deleteGoodsList(@RequestBody String json){
        try{
            ViewVO viewVO = getFormJSON(json, ViewVO.class);
            if(viewVO == null || viewVO.getIds() == null || viewVO.getIds().size() <= 0){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            goodsService.deleteGoodsList(viewVO.getIds(),String.valueOf(getUserId(json)));
            return super.getResultJSONStr(true, null, "");
        }catch(Exception e){
            log.error("[goods][deleteGoodsList] Exception:{}" , e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }



}
