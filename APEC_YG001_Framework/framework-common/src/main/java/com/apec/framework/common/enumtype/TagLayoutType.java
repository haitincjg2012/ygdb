package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 *
 * 标签位置类型
 * Created by yirpro on 2017/9/10.
 */
public enum TagLayoutType implements BaseEnum {

    FIRST_LAY("头部显示"),
    NEXT_LAY("次级显示");

    private final String key;

    private TagLayoutType(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}