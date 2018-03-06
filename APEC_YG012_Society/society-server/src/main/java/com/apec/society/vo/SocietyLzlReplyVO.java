package com.apec.society.vo;

import com.apec.framework.dto.BaseVO;
import lombok.Data;

/**
 * Created by hmy on 2017/10/30.
 *
 * @author hmy
 */
@Data
public class SocietyLzlReplyVO extends BaseVO<Long> {

    /**
     * 帖子ID
     */
    private Long societyPostId;

    /**
     * 帖子评论的id(楼层id)
     */
    private Long toFuReplyId;

    /**
     * 回复对象id
     */
    private Long toReplyId;

    /**
     * 回复人ID
     */
    private Long userId;

    /**
     * 	回复对象uid
     */
    private Long toUserId;

    /**
     * 回复内容
     */
    private String content;

    /**
     * 是否来源于系统
     */
    private Boolean formSystem;


}
