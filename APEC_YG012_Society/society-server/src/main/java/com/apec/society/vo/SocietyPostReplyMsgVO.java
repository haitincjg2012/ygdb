package com.apec.society.vo;

import com.apec.framework.common.enums.Realm;
import com.apec.framework.common.enumtype.ArticleMsgType;
import com.apec.framework.rockmq.vo.IMqBody;
import lombok.Data;

import java.util.Date;

/**
 * Created by hmy on 2017/10/31.
 *
 * @author hmy
 */
@Data
public class SocietyPostReplyMsgVO implements IMqBody{

    private Long id;

    private Date createDate;

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
     * 关联的对象来源id
     */
    private Long relativeMainId;

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

    /**
     * 是否来源于系统
     */
    private Boolean formSystem;


}
