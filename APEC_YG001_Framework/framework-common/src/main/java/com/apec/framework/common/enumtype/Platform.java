package com.apec.framework.common.enumtype;

/**
 * 类 编 号：
 * 类 名 称：
 * 内容摘要：
 * 创建日期：2016-09-28 11:49
 * 编码作者：
 */
public enum Platform {

    IOS,
    ANDROID,
    WEB;

    public static Platform getPlatform(String platform) {
        switch (platform.toUpperCase()){
            case "IOS":return Platform.IOS;
            case "ANDROID":return Platform.ANDROID;
            case "WEB":return Platform.WEB;
            default:return null;
        }
    }
}
