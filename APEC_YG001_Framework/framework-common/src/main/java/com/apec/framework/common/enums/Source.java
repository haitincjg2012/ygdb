package com.apec.framework.common.enums;

/**
 * Title:信息来源</p>
 * Source 是具体的物理来源
 * @author yirde  2017/4/12.
 */
public enum Source implements  BaseEnum {

    /**
     * PC端
     */
   PC("PC端"),

    /**
     * APP端
     */
    APP("APP端"),

    /**
     * 移动端
     */
    MOBILE("移动端"),

    /**
     * 微信端
     */
    WEIXIN("微信端"),

    /**
     * 后台
     */
    BACK("后台");

    private final String key;

    @Override
    public String getKey(){ return key; }

    Source(final String key){
        this.key = key;
    }
}
