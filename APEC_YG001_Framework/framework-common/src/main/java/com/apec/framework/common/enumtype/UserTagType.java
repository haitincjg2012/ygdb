package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Title: 用户标签
 *
 * @author yirde  2017/9/6.
 */
public enum UserTagType implements BaseEnum {

    /**
     * 供应链金融合作库
     */
    GYLJRHZK("供应链金融合作库"),

    /**
     * 企业认证
     */
    QYRZ("企业认证"),

    /**
     * 调果代办
     */
    DGDB("调果代办"),

    /**
     * 收果代办
     */
    SGDB("收果代办"),

    /**
     * 加盟代办
     */
    JMDB("加盟代办");

    private final String key;

    UserTagType(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}