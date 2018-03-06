package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Created by hmy on 2017/10/24.
 * @author hmy
 * 上榜条件
 */
public enum ConditionsType implements BaseEnum{

    /**
     * 发布供求
     */
    PUBLISH_PRODUCT("发布供求"),

    /**
     * 发表评论
     */
    GIVE_THE_COMMENT("发表评论"),

    /**
     * 发布帖子
     */
    PUBLISH_SOCIETYPOST("发布帖子"),

    /**
     * 调果量
     */
    VOUCHER_TOTALNUM("调果量"),

    /**
     * 调果单数
     */
    VOUCHER_NUM("调果单数"),

    /**
     * 访问频率
     */
    ACCESS_FREQUENCY("访问频率");

    private final String key;

    ConditionsType(String key){
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
