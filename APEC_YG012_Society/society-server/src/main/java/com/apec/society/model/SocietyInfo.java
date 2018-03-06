package com.apec.society.model;

import com.apec.framework.common.enumtype.SocietyType;
import com.apec.framework.mongodb.model.BaseModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Title: 圈子信息表
 *
 * @author yirde  2017/10/19.
 */
@Getter
@Setter
@Document
public class SocietyInfo extends BaseModel<Long> {

    private static final long serialVersionUID = -5993252743301819040L;
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
