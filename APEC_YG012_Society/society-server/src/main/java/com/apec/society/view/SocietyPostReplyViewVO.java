package com.apec.society.view;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by hmy on 2017/10/30.
 * 帖子回复
 * @author hmy
 */
@Data
public class SocietyPostReplyViewVO {

    /**
     * id
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 创建时间
     */
    private String createDateStr;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户头像
     */
    private String imgUrl;

    /**
     * 内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 是否点赞
     */
    private Boolean likeUser;

    /**
     * 楼中楼回复
     */
    private List<SocietyLzlReplyViewVO> societyLzlReplyViewVOS;

    /**
     * 是否来源于系统
     */
    private Boolean formSystem;


}
