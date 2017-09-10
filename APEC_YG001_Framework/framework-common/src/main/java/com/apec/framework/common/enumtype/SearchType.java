package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Created by hmy on 2017/7/31.
 */
public enum SearchType implements BaseEnum {

    RMSS("热门搜索"),
    YZSS("预制搜索");

    private final String key;

    private SearchType(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }



}
