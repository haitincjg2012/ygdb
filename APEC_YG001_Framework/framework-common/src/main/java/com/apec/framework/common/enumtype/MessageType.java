package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Title: 消息类型
 *
 * @author yirde  2017/6/30.
 */
public enum MessageType implements BaseEnum {

    /**
     * seen by receiver
     */
    NOTIFICATION("系统通知"),
    /**
     * seen by sender and receiver
     */
    MESSAGE("私信"),

    /**
     * plat send short message
     */
    SHORTMESSAGE("短信");

    private final String key;

    private MessageType(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}