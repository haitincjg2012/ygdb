package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Title: 用户标签
 *
 * @author yirde  2017/9/6.
 */
public enum UserTagType implements BaseEnum {

    GYLJRHZK("供应链金融合作库"),
    QYRZ("企业认证");

    private final String key;

    private UserTagType(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}