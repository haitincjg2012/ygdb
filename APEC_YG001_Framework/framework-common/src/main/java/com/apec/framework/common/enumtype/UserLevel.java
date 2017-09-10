package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Title: 用户等级
 *
 * @author yirde  2017/6/28.
 */
public enum UserLevel implements BaseEnum {

    QT("青铜"),

    BY("白银"),

    HJ("黄金"), //冻结不能登陆

    BJ("铂金"),

    ZS("钻石"), //

    DS("大师");

    private final String key;

    private UserLevel(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}