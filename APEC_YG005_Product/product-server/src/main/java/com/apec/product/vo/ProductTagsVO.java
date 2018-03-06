package com.apec.product.vo;

import com.apec.framework.common.enumtype.TagLayoutType;
import lombok.Data;


/**
 * Created by hmy on 2018/1/18.
 *
 * @author hmy
 */
@Data
public class ProductTagsVO {

    private Long id;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 样式名称
     */
    private String className;

    /**
     * 标签对应的布局类型
     */
    private TagLayoutType tagLayoutType;


    /**
     * 排序
     */
    private int sort;

    /**
     * es_id
     */
    private String esId;

}
