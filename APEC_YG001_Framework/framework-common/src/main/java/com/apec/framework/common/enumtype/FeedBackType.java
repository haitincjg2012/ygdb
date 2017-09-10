package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Created by hmy on 2017/8/28.
 */
public enum FeedBackType implements BaseEnum{

    JB("举报"),
    FK("反馈");

    private final String key;

    private FeedBackType(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

}
