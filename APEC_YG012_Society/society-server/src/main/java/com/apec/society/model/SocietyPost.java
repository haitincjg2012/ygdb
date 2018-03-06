package com.apec.society.model;

import com.apec.framework.common.enums.Realm;
import com.apec.framework.mongodb.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Title: 圈子帖子表
 *
 * @author yirde  2017/10/20.
 */
@Getter
@Setter
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
     * 转发数
     */
    private Integer transCount;

    /**
     * 是否置顶
     */
    private Boolean isTop;

    /**
     * 关联的id，如转发文章或别人的帖子
     */
    private Long relativeId;

    /**
     * 来源。ARTICLE/SOCIETYPOST
     */
    private Realm  realm;

    /**
     * 是否含有图片
     */
    private Boolean hasImage;

    /**
     * 作者
     */
    private String author;

    /**
     * 是否为个人发布，默认为平台系统发布
     */
    private Boolean personPub;

    /**
     * 审核状态，Y为审核通过，N为审核拒绝，0为未审核
     */
    private String auditState;

    /**
     * 审核通过时间
     */
    private Date passDate;

    /**
     * 第一张图片路径/行情仅此一张
     */
    private String url;

    /**
     * 额外信息,置顶
     */
    private String priv;

    /**
     * 备注
     */
    private String remark;

    /**
     * 上线时间
     */
    private Date startTime;

    /**
     * 下线时间
     */
    private Date endTime;

    /**
     * 期号
     */
    private String issue;

}
