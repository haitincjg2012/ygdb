package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Title: 用户身份
 *
 * @author yirde  2017/6/28.
 */
public enum UserType implements BaseEnum {

    DB("代办"),
    KS("客商"),
    LK("冷库"),
    ZZH("果农"),
    HZS("合作社");

    private final String key;

    @Override
    public String getKey(){ return key; }

    UserType(final String key){
        this.key = key;
    }
}
