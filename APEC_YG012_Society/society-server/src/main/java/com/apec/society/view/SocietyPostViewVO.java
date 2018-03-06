package com.apec.society.view;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by hmy on 2017/10/30.
 *
 * @author hmy
 */
@Data
public class SocietyPostViewVO {

    /**
     * id
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 圈子ID
     */
    private Long  societyId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 创建人姓名
     */
    private String name;

    /**
     * 上传人手机号
     */
    private String mobile;

    /**
     * 创建人头像
     */
    private String imgUrl;

    /**
     * 创建人所在组织id
     */
    private Long userOrgId;

    /**
     * 是否关注
     */
    private Boolean attentionUser;

    /**
     * 是否点赞
     */
    private Boolean likeUser;

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
     * 作者
     */
    private String author;

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
     * 第一张图片路径/行情仅此一张
     */
    private String url;

    /**
     * 是否含有图片
     */
    private Boolean hasImage;

    /**
     * 额外信息,置顶
     */
    private String priv;

    /**
     * 发布时间字符（"一天前","刚刚"等等）
     */
    private String pubDateStr;

    /**
     * 帖子的图
     */
    private List<SocietyPostImagesViewVO> societyPostImagesViewVOS;

    /**
     * 帖子的楼层回复
     */
    private List<SocietyPostReplyViewVO> societyPostReplyViewVOS;

}
