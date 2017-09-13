package com.apec.user.vo;

import com.apec.framework.common.enumtype.TagLayoutType;
import lombok.Data;

/**
 * Created by hmy on 2017/9/13.
 */
@Data
public class UserTagsVO {

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
     * OrgId 用户组织ID
     */
    private Long  userOrgId;

}
