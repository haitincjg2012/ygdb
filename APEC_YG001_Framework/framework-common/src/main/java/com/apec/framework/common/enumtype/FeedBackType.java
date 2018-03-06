package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Created by hmy on 2017/8/28.
 * @author hmy
 */
public enum FeedBackType implements BaseEnum{

    /**
     * 举报
     */
    JB("举报"),

    /**
     * 反馈
     */
    FK("反馈");

    private final String key;

    FeedBackType(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

}
