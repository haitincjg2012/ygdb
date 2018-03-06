package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Created by hmy on 2017/10/24.
 * @author hmy
 * 统计类别
 */
public enum StatisticCategories implements BaseEnum{

    /**
     * 上月
     */
    LAST_MONTH("上月"),

    /**
     * 累计（本年）
     */
    TOTAL("累计");

    private final String key;

    StatisticCategories(String key){
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
