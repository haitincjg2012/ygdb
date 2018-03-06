package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * @author  xxx
 * 标签位置类型
 * Created by yirpro on 2017/9/10.
 */
public enum TagLayoutType implements BaseEnum {

    /**
     * 头部显示
     */
    FIRST_LAY("头部显示"),

    /**
     * 次级显示
     */
    NEXT_LAY("次级显示");

    private final String key;

    TagLayoutType(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}