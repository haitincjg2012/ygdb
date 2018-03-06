package com.apec.society.dto;

import com.apec.framework.dto.BaseDTO;
import lombok.Data;

import java.util.List;

/**
 * Created by hmy on 2017/10/31.
 *
 * @author hmy
 */
@Data
public class SocietyPostReplyDTO extends BaseDTO {

    /**
     * id
     */
    private Long id;

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
     * ids
     */
    private List<Long> ids;

}
