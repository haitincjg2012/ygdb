package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * @author wubi
 * @Date 2017-09-22
 */
public enum EsConfigStatus implements BaseEnum {

    /**
     * 已使用
     */
     USED("1","已使用"),

    /**
     * 未使用
     */
     NOT_USED("0","未使用") ;

    private final String key;
    private final String desc;

    EsConfigStatus(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    @Override
    public String getKey() {
        return key;
    }

}