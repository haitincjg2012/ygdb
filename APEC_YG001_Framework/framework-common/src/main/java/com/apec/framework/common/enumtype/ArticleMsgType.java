package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Created by hmy on 2017/10/24.
 * @author hmy
 * 行情消息类型
 */
public enum ArticleMsgType implements BaseEnum{

    /**
     * 点赞
     */
    GIVE_THE_THUMBS_UP("点赞"),

    /**
     * 发表评论
     */
    GIVE_THE_COMMENT("发表评论");

    private final String key;

    ArticleMsgType(String key){
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
