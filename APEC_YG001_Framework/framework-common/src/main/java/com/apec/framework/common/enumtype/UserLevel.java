package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Title: 用户等级
 *
 * @author yirde  2017/6/28.
 */
public enum UserLevel implements BaseEnum {

    /**
     * 青铜
     */
    QT("青铜"),

    /**
     * 白银
     */
    BY("白银"),

    /**
     * 黄金
     */
    HJ("黄金"),

    /**
     * 铂金
     */
    BJ("铂金"),

    /**
     * 钻石
     */
    ZS("钻石"),

    /**
     * 大师
     */
    DS("大师");

    private final String key;

    UserLevel(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}