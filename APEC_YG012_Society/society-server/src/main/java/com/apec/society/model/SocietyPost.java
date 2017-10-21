package com.apec.society.model;

import com.apec.framework.mongodb.model.BaseModel;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Title: 圈子帖子表
 *
 * @author yirde  2017/10/20.
 */
@Data
@Document
public class SocietyPost extends BaseModel<Long> {

   private static final long serialVersionUID = -2226842898892649122L;

    /**
     * 圈子ID
     */
    private Long  societyId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 标签
     */
    private String label;

    /**
     * 最近回复时间
     */
    private Date lastReplyTime;

    /**
     * 阅读量
     */
    private Integer viewCount;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 评论量
     */
    private Integer replyCount;

    /**
     * 是否置顶
     */
    private Boolean isTop;
}
