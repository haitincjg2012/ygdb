package com.apec.cms.vo;

import com.apec.framework.common.enumtype.CategoryType;
import com.apec.framework.dto.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Title:
 *
 * @author yirde  2017/7/27.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelVO extends BaseVO<Long> {

    /**
     * 栏目名称
     */
    private String name;

    /**
     * 栏目类别
     */
    private CategoryType category;

    /**
     * 描述
     */
    private String description;

    private String code;
}
