package com.apec.society.dto;

import com.apec.framework.common.enums.Realm;
import com.apec.framework.dto.BaseDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by hmy on 2017/10/30.
 *
 * @author hmy
 */
@Data
public class SocietyPostDTO extends BaseDTO {

    private Long id;

    /**
     * 圈子ID
     */
    private Long  societyId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 期号
     */
    private String issue;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 是否置顶
     */
    private Boolean isTop;

    /**
     * 用户ids
     */
    private List<Long> userIds;

    /**
     * 是否查询关注
     */
    private Boolean attentionUser;

    /**
     * 列表页所展示的评论的条数
     */
    private Integer replyNum;

    /**
     * 来源。ARTICLE/SOCIETYPOST
     */
    private Realm realm;

    /**
     * 开始时间
     */
    private Date beginDate;

    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * 是否为个人发布，默认为平台系统发布
     */
    private String personPub;

    /**
     * 审核状态
     */
    private String auditState;

    /**
     * 额外信息
     */
    private String priv;

    /**
     * 作者
     */
    private String author;


}
