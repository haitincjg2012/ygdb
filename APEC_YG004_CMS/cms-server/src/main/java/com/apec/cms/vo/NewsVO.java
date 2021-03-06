package com.apec.cms.vo;

import com.apec.framework.common.enumtype.CategoryType;
import com.apec.framework.dto.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by wubi on 2017/9/13.
 * @author wubi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsVO extends BaseVO<Long> {

    private Long channelId;

    private String channelCode;

    private String channelName;


    /**
     * 新闻标题
     */
    private String title;

    /**
     * 栏目类别
     */
    private CategoryType category;

    /**
     * 是否含有图片
     */
    private Boolean hasImage;

    /**
     * 是否显示
     */
    private Boolean priority;

    /**
     * 图片URL
     */
    private String url;

    /**
     * 发布时间
     */
    private Date pubDate;

    /**
     * 作者
     */
    private String author;

    /**
     * Meiti
     */
    private String media;


    /**
     * 展示顺序
     */
    private Integer ordinal;

    /**
     * 地址信息
     */
    private String address;

    /**
     * 额外信息(0,1,2,3,4)
     */
    private String priv;

    /**
     * 是否为个人发布，默认为平台系统发布
     */
    private Boolean personPub;

    /**
     * 审核状态
     */
    private String auditState;

    /**
     * 审核通过时间
     */
    private Date passDate;

    /**
     * 阅读数
     */
    private String readNum;

    /**
     * 发布时间字符（"一天前","刚刚"等等）
     */
    private String pubDateStr;

    private String content;


}
