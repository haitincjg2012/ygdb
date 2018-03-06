package com.apec.society.vo;

import com.apec.framework.dto.BaseVO;
import lombok.Data;

/**
 * Created by hmy on 2017/10/30.
 *
 * @author hmy
 */
@Data
public class SocietyPostReplyVO extends BaseVO<Long>{

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
     * 是否点赞
     */
    private Boolean likePostReply;

    /**
     * 是否来源于系统
     */
    private Boolean formSystem;

}
