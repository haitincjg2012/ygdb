package com.apec.framework.common.tools;

import java.util.UUID;

/**
 * uuid生成器
 * @author gsw
 *
 */
public class UuidGenerator
{
    /**
     * 生成唯一标示uuid 没有中划线
     */
    public static String getUuidGenerator() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }
    /**
     * 生成唯一标示uuid 带中划线
     */
    public static String getUuidWithLine() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }

}
