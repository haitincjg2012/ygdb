package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Title: 抽奖形式
 * @author hmy  2017/12/20.
 */
public enum LotteryFormType implements BaseEnum {

    /**
     * 大转盘
     */
    BIG_WHEEL("大转盘"),

    /**
     * 刮刮乐
     */
    SCRATCH("刮刮乐"),

    /**
     * 砸金蛋
     */
    GOLD_EGG("砸金蛋");

    private final String key;

    LotteryFormType(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

}