package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Title: 用户状态
 *
 * @author yirde  2017/6/28.
 */
public enum UserStatus implements BaseEnum {

    /**
     * 未实名认证
     */
    UNREALAUTH("未实名认证"),

    /**
     * 正常
     */
    NORMAL("正常"),

    /**
     * //冻结不能登陆  //
     */
    FREEZE("已冻结");

    private final String key;

    UserStatus(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
