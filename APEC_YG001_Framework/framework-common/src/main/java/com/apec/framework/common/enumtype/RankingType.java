package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Created by hmy on 2017/10/24.
 * @author hmy
 * 排行榜类型
 */
public enum RankingType implements BaseEnum{

    /**
     * 活跃达人
     */
    ACTIVE_MAN("活跃达人"),

    /**
     * 调果达人
     */
    FRUITING_PEOPLE("调果达人");

    private final String key;

    RankingType(String key){
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
