package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Created by hmy on 2017/10/24.
 * @author hmy
 * 参与状态
 */
public enum ParticipationState implements BaseEnum{

    /**
     * 中奖
     */
    GET_PRIZE("中奖"),

    /**
     * 没中奖
     */
    NOT_GET_PRIZE("没中奖"),

    /**
     * 无法参与
     */
    UNABLE_PARTICIPATE("无法参与");

    private final String key;

    ParticipationState(String key){
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
