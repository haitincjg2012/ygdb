package com.apec.cms.vo;

import com.apec.framework.common.enumtype.CategoryType;
import com.apec.framework.dto.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Title:
 *
 * @author yirde  2017/7/28.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleVO extends BaseVO<Long> {

    private Long channelId;

    private String channelCode;

    /**
     * 新闻标题
     */
    private String title;

    /**
     * 栏目类别
     */
    private CategoryType category;

    /**
     * 新闻内容,也用作图片路径
     */
    private String content;

    /**
     * 是否含有图片
     */
    private boolean hasImage;

    /**
     * 是否显示
     */
    private boolean priority;

    /**
     * 新闻id
     */
    private String newsId;

    /**
     * 新闻url
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
     *
     * @return
     */
    private String address;

    /**
     * 额外信息
     *
     * @return
     */
    private String priv;


}
