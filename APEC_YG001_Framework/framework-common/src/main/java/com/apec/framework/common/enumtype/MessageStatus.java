package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Title：消息状态
 *
 * @author yirde  2017/6/30.
 */
public enum MessageStatus  implements BaseEnum {

    /**
     * 新消息
     */
    NEW("新消息"),

    /**
     * 已读
     */
    READ("已读"),

    /**
     * 已存档
     */
    ARCHIVED("已存档"),

    /**
     * 发件人已删除message,但收件人仍然可以看到
     */
    DELETED_BY_SENDER("发送人已刪除"),

    /**
     * 收件人已删除message，但发件人仍然可以看到
     */
    DELETED_BY_RECEIVER("接收人已删除"),

    /**
     * TODO目前支持的刪除操作，一旦删除那么sender和receiver将都不可见
     */
    DELETED("已刪除"),

    /**
     * 信息发送成功（目前该状态只应用于MessageType.SHORTMESSAGE）
     */
    SENT_SUCCESS("发送成功"),

    /**
     * 信息发送失败（目前该状态只应用于MessageType.SHORTMESSAGE）
     */
    SENT_FAILED("发送失败");

    private final String key;

    MessageStatus(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
