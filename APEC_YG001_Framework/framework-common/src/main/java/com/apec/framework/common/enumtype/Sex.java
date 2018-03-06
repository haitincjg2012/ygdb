package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * @author hmy
 * Created by hmy on 2017/7/6.
 */
public enum Sex implements BaseEnum {

    /**
     * 先生
     */
    MALE("M", "先生"),

    /**
     * 女士
     */
    FEMALE("F", "女士");

    private final String key;

    private final String value;

    Sex(final String key, final String value){
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

}
