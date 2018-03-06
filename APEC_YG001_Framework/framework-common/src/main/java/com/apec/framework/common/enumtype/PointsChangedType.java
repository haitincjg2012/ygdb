package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * 积分变化类型
 * Created by hmy on 2017/7/13.
 * @author hmy
 */
public enum PointsChangedType implements BaseEnum {

    /**
     * 加分
     */
    PLUS("加分"),

    /**
     * 减分
     */
    REDUCTION("减分");

    private final String key;

    PointsChangedType(final String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
