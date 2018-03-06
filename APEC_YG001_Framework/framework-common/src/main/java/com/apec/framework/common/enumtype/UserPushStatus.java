package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Title: 用户状态
 *
 * @author yirde  2017/6/28.
 */
public enum UserPushStatus implements BaseEnum {

    /**
     * PUSH
     */
    PUSH("1"),

    /**
     * NO_PUSH
     */
    NO_PUSH("0");

    private final String key;

    private UserPushStatus(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
