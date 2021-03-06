package com.apec.society.model;

import com.apec.framework.mongodb.model.BaseModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Title: 贴子回复
 *
 * @author yirde  2017/10/20.
 */
@Getter
@Setter
@Document
public class SocietyPostReply extends BaseModel<Long>{

   private static final long serialVersionUID = 3553587476997963414L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 帖子ID
     */
    private Long societyPostId;

    /**
     * 内容
     */
    private String content;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 回复数
     */
    private Integer replyNum;

    /**
     * 是否置顶
     */
    private Boolean isTop;

    /**
     * 是否来源于系统
     */
    private Boolean formSystem;

    /**
     * 点赞数
     */
    private Integer likeCount;

}
