package com.apec.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Title:  ES 组织信息VO
 *
 * @author yirde  2017/9/12.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ESOrgInfoVO {

    /**
     * 仓库组名称
     */
    private String orgName;

    /**
     * 组织的缩略图
     */
    private String orgFirstBannerUrl;

    /**
     * 所在地区
     */
    private String  address;

    /**
     * 排序权重
     */
    private Long orderWeight;

    /**
     * ID
     */
    private Long orgId;

    /**
     * 标签集合
     */
    private List<ESTagsInfoVO> orgTags ;

    private Date createDate;

}
