package com.apec.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Title:
 *
 * @author yirde  2017/7/20.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ESProductImageVO {

    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 顺序
     */
    private Integer sort;
}
