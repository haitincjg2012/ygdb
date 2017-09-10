package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Created by wubi on 2017/8/7.
 */
public enum OrderWeightType implements BaseEnum{
    /**
     * 收藏量
     * */
    SAVE_NUM("SAVE_NUM"),
    /**
     * 浏览量
     */
    VIEW_NUM("VIEW_NUM"),

    /**
     * 下架时间
     */
    TIME_OUT("TIME_OUT"),

    /**
     * 是否推荐
     */
    RRECOMMEND("RRECOMMEND");


    private final String key;

    private OrderWeightType(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
