package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Title: 用户实名认证
 *
 * @author yirde  2017/6/28.
 */
public enum UserRealAuth  implements BaseEnum {

    /**
     * 未实名认证
     */
    UNREALAUTH("未实名认证"),

    /**
     * 实名认证中
     */
    AUDITING("实名认证中"),

    /**
     * 实名认证未通过
     */
    REJECTED("实名认证未通过"),

    /**
     * 实名认证通过
     */
    NORMAL("实名认证通过");

    private final String key;

    UserRealAuth(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
