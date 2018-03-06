package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Title: 用户身份
 *
 * @author yirde  2017/6/28.
 */
public enum UserType implements BaseEnum {

    /**
     * 代办
     */
    DB("代办"),

    /**
     * 客商
     */
    KS("客商"),

    /**
     * 冷库
     */
    LK("冷库"),

    /**
     * 果农
     */
    ZZH("果农"),

    /**
     * 合作社
     */
    HZS("合作社");

    private final String key;

    @Override
    public String getKey(){ return key; }

    UserType(final String key){
        this.key = key;
    }
}
