package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Created by hmy on 2017/10/24.
 * @author hmy
 * 中奖限制
 */
public enum LimitedAward implements BaseEnum{

    /**
     * 每人限制一个
     */
    PEOPLE_ONE("每人限制一个"),

    /**
     * 每人限制三个
     */
    PEOPLE_THREE("每人限制三个"),

    /**
     * 不限
     */
    UNLIMITED("不限");

    private final String key;

    LimitedAward(String key){
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
