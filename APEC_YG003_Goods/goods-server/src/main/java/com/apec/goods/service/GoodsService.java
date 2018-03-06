package com.apec.goods.service;

import com.apec.framework.common.PageDTO;
import com.apec.goods.vo.GoodsVO;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * 商品管理
 * @author hmy
 */
public interface GoodsService {

    /**
     * 添加商品对象
     * @param goodsVO 商品对象
     * @param userId 操作人id
     * @return 处理结果
     */
    String saveGoods(GoodsVO goodsVO,String userId);

    /**
     * 修改商品对象
     * @param goodsVO 商品对象
     * @param userId 操作人id
     * @return 处理结果
     */
    String updateGoods(GoodsVO goodsVO,String userId);

    /**
     * 删除商品对象
     * @param goodsVO 商品对象
     * @param userId 操作人id
     * @return 处理结果
     */
    String removeGoods(GoodsVO goodsVO,String userId);

    /**
     * 查询商品具体信息
     * @param goodsVO 商品对象
     * @return 商品对象
     */
    GoodsVO getGoods(GoodsVO goodsVO);

    /**
     * 分页查询商品信息
     * @param goodsVO 商品对象查询条件
     * @param pageRequest 分页对象
     * @return 分页结果
     */
    PageDTO<GoodsVO> searchGoodsPage(GoodsVO goodsVO, PageRequest pageRequest);

    /**
     * 批量删除goods对象
     * @param ids 要删除的对象集合ids
     * @param userId 操作人id
     * @return 处理结果
     */
    String deleteGoodsList(List<Long> ids,String userId);

}
