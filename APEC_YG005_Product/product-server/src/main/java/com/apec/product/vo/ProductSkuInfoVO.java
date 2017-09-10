package com.apec.product.vo;

import com.apec.framework.common.enumtype.UserType;
import com.apec.framework.dto.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Title:
 *
 * @author yirde  2017/7/20.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSkuInfoVO extends BaseVO<Long> {

    private Long userId;

    /**
     * 发布人的姓名
     */
    private String userName;

    private String mobile;

    private UserType userType;

    /**
     * 需求 属性
     */
    private List<ProductAttrVO> productAttrs;

    private String skuName;

}
