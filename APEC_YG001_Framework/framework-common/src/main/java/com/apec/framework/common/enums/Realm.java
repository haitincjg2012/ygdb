package com.apec.framework.common.enums;

/**
 * Title: 定义实体相关的域
 * @author yirde  2017/6/28.
 */
public enum Realm implements BaseEnum {

    USER("用戶"),

    /**
     * CMS管理
     */
    CHANNEL("CMS栏目"),

    ARTICLE("CMS文章"),

    IMAGE("图片"),

    FILE("文件"),

    MESSAGE("消息"),

    ACTIVITY("活动"),

    POINT("积分"),

    USER_SHARE("用户分享");

    private final String key;

    private Realm(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
