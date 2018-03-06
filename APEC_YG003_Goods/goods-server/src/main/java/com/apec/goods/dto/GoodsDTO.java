package com.apec.goods.dto;

import com.apec.framework.dto.BaseDTO;
import com.apec.goods.vo.GoodsAttrVO;
import lombok.Data;

import java.util.List;

/**
 * Title:GoodsDTO
 *
 * @author yirde  2017/3/23
 */
@Data
public class GoodsDTO extends BaseDTO {

    /**
     * 商品大类编码
     */
    private String goodsCode;

    /**
     * 商品大类名称
     */
    private String goodsName;

    /**
     * 描述
     */
    private String remark;

    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 商品属性
     */
    private List<GoodsAttrVO> goodsAttrVOList;

}
