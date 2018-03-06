package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Created by hmy on 2017/10/24.
 * @author hmy
 * 行情消息类型
 */
public enum ParticipationNumType implements BaseEnum{

    /**
     * 每日一次
     */
    DAILY_ONE("每日一次"),

    /**
     * 每日两次
     */
    DAILY_TWO("每日两次"),

    /**
     * 一人一次
     */
    PEOPLE_ONE("一人一次");

    private final String key;


    ParticipationNumType(String key){
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

}
