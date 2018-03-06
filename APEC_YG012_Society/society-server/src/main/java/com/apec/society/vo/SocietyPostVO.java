package com.apec.society.vo;

import com.apec.framework.common.enums.Realm;
import com.apec.framework.dto.BaseVO;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by hmy on 2017/10/30.
 *
 * @author hmy
 */
@Data
public class SocietyPostVO extends BaseVO<Long>{

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
     * 点赞/取消点赞
     */
    private Boolean likeSocietyPost;

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
    private Realm realm;

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
     * 第一张图片路径/行情仅此一张
     */
    private String url;

    /**
     * 备注
     */
    private String remark;

    /**
     * 帖子上传的图片
     */
    private List<SocietyPostImagesVO> societyPostImagesVOS;



}
