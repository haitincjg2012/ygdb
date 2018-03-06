package com.apec.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by hmy on 2018/1/19.
 *
 * @author hmy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ESProductTagsVO {

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 样式名称
     */
    private String className;

    /**
     * 排序
     */
    private int sort;

}
