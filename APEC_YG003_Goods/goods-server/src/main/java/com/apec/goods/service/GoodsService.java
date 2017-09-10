package com.apec.goods.service;

import com.apec.framework.common.PageDTO;
import com.apec.goods.vo.GoodsVO;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * 用户相关服务
 */
public interface GoodsService {

    /**
     * 添加商品
     */
    String saveGoods(GoodsVO goodsVO,String userId);

    /**
     * 修改商品
     */
    String updateGoods(GoodsVO goodsVO,String userId);

    /**
     * 删除商品
     */
    String removeGoods(GoodsVO goodsVO,String userId);

    /**
     * 查询商品具体信息
     * @param goodsVO
     * @return
     */
    GoodsVO getGoods(GoodsVO goodsVO);

    /**
     * 分页查询商品信息
     */
    PageDTO<GoodsVO> searchGoodsPage(GoodsVO goodsVO, PageRequest pageRequest);

    /**
     * 批量删除goods
     * @param ids
     * @return
     */
    String deleteGoodsList(List<Long> ids,String userId);

}
