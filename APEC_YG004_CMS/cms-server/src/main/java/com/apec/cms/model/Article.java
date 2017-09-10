package com.apec.cms.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.CategoryType;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Title: 新闻
 *
 * @author yirde  2017/6/29.
 */
@Entity
@Data
@Table(name = "CMS_ARTICLE")
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class Article extends BaseModel<Long> {

    private static final long serialVersionUID = 6377891533833240242L;

    @ManyToOne
    @JoinColumn(name = "CHANNEL_ID")
    private Channel channel;

    /**
     * 新闻标题
     */
    @Column(nullable = false)
    private String title;

    /**
     * 栏目类别
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryType category;

    /**
     * 新闻内容,也用作图片路径
     */
    @Lob
    @Column(nullable = true)
    private String content;

    /**
     * 是否含有图片
     */
    @Column(nullable = true)
    private boolean hasImage;

    /**
     * 是否显示
     */
    @Column(nullable = true)
    private boolean priority;

    /**
     * 新闻id
     */
    @Column(nullable = true)
    private String newsId;

    /**
     * 新闻url
     */
    @Column(nullable = true)
    private String url;

    /**
     * 发布时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date pubDate;

    /**
     * 作者
     */
    @Column(nullable = true)
    private String author;

    /**
     * Meiti
     */
    @Column(nullable = true)
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
