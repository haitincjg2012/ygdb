package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Title: 圈子类型
 *
 * @author yirde  2017/10/20.
 */
public enum SocietyType implements BaseEnum {

    DEFAULT("默认圈子"),
    HOT_TOPIC("热门话题");

    private final String key;

    SocietyType(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
