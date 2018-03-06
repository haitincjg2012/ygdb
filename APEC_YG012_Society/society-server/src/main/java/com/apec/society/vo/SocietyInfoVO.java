package com.apec.society.vo;

import com.apec.framework.common.enumtype.SocietyType;
import com.apec.framework.mongodb.vo.BaseVO;
import lombok.Data;

/**
 * Title: SocietyInfo VO
 *
 * @author yirde  2017/10/20.
 */
@Data
public class SocietyInfoVO extends BaseVO<Long> {

    /**
     *  圈子标题
     */
    private String title;

    /**
     * 帖子数
     */
    private Integer  postCount;

    /**
     * 关注数/围观数
     */
    private Integer attentionCount;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * Log 地址
     */
    private String logo;

    /**
     * 背景地址
     */
    private String background;

    /**
     * 描述
     */
    private String description;

    /**
     * 圈子类型
     */
    private SocietyType societyType;
}
