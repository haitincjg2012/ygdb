package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Created by hmy on 2017/10/24.
 * @author hmy
 * 奖项
 */
public enum PrizeNobel implements BaseEnum{

    /**
     * 一等奖
     */
    FIRST_AWARD("一等奖"),

    /**
     * 二等奖
     */
    SECOND_AWARD("二等奖"),

    /**
     * 三等奖
     */
    THIRST_AWARD("三等奖"),

    /**
     * 安慰奖
     */
    CONSOLATION_AWARD("安慰奖");

    private final String key;

    PrizeNobel(String key){
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
