package com.apec.framework.common.enums;

/**
 * Title: 定义实体相关的域
 * @author yirde  2017/6/28.
 */
public enum Realm implements BaseEnum {

    /**
     * 用户
     */
    USER("用戶"),

    /**
     * CMS管理
     */
    CHANNEL("CMS栏目"),

    /**
     * CMS文章
     */
    ARTICLE("CMS文章"),

    /**
     * 图片
     */
    IMAGE("图片"),

    /**
     * 文件
     */
    FILE("文件"),

    /**
     * 消息
     */
    MESSAGE("消息"),

    /**
     * 活动
     */
    ACTIVITY("活动"),

    /**
     * 积分
     */
    POINT("积分"),

    /**
     * 供求
     */
    PRODUCT("供求"),

    /**
     * 交收单
     */
    VOCHER("交收单"),

    /**
     * 帖子
     */
    SOCIETYPOST("帖子"),

    /**
     * 行情竞猜
     */
    QUOTATION("行情竞猜"),

    /**
     * 用户分享
     */
    USER_SHARE("用户分享");

    private final String key;

    Realm(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
