package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Title: 规则组
 *
 * @author yirde  2017/6/28.
 */
public enum RuleGroup implements BaseEnum {

    /**
     * 积分规则
     */
    LOGIN("01", "登陆"),
    COMPLETE_INFO("02", "完善信息"),
    SEND_ORDER("03","上传单据"),
    SEND_REQUEST("04", "发布供求"),
    SIGN_IN("05", "签到"),
    PROMOTION("06", "推广"),

    /**
     * 积分消耗规则
     */
    EXCHANGE("01", "兑换"),
    LOTTERY("02", "抽奖"),
    TRIGGER("03", "触发条件"),
    PUNISHMENT("04","处罚");

    private final String key;
    private final String type;

    private RuleGroup(String key, String code) {
        this.key = key;
        this.type = code;
    }


    @Override
    public String getKey() {
        return this.key;
    }


    public String getType() {
        return this.type;
    }
}