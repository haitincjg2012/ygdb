package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Title: RocketMq 发送状态
 *
 * @author yirde  2017/7/1.
 */
public enum MQSendStatus implements BaseEnum {

    /**
     * 发送失败
     */
     FAILURE("发送失败"),

    /**
     *  //远程地址访问
     */
     SUCCESS("发送成功") ;

    private final String key;

    MQSendStatus(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

}