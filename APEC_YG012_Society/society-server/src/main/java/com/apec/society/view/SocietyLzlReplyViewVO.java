package com.apec.society.view;

import lombok.Data;

import java.util.Date;

/**
 * Created by hmy on 2017/10/30.
 *
 * @author hmy
 */
@Data
public class SocietyLzlReplyViewVO {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 回复人ID
     */
    private Long userId;

    /**
     * 回复人名称
     */
    private String name;

    /**
     * 	回复对象uid
     */
    private Long toUserId;

    /**
     * 回复对象名称
     */
    private String toUserName;

    /**
     * 回复内容
     */
    private String content;

    /**
     * 是否来源于系统
     */
    private Boolean formSystem;

}
