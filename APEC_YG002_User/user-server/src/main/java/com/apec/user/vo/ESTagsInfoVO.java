package com.apec.user.vo;

import com.apec.framework.common.enumtype.TagLayoutType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Title: ES 标签
 *
 * @author yirde  2017/9/12.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ESTagsInfoVO {


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
     *
     */
    private Integer sort;

}
