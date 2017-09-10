package com.apec.product.vo;

import com.apec.framework.common.enumtype.ImageDefType;
import com.apec.framework.dto.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Title:
 *
 * @author yirde  2017/7/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageVO  extends BaseVO<Long> {

    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 图片是否默认
     */
    private ImageDefType imageDefType;

}
