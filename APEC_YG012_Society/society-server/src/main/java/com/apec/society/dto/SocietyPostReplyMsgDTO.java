package com.apec.society.dto;

import com.apec.framework.common.enums.Realm;
import com.apec.framework.common.enumtype.ArticleMsgType;
import com.apec.framework.dto.BaseDTO;
import lombok.Data;

import java.util.Date;

/**
 * Created by hmy on 2017/11/2.
 *
 * @author hmy
 */
@Data
public class SocietyPostReplyMsgDTO extends BaseDTO {

    private Long id;

    /**
     * 接收者id
     */
    private Long receiver;

    /**
     * 发送者id
     */
    private Long sender;

    /**
     * 发送者名称
     */
    private String senderName;

    /**
     * 关联的id
     */
    private Long relativeId;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 消息类型
     */
    private ArticleMsgType articleMsgType;

    /**
     * 已读状态，true为已读，false为未读
     */
    private Boolean readFlag;

    /**
     * 来源
     */
    private Realm realm;


}
