package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Title: MQ 消费的状态
 *
 * @author yirde  2017/7/4.
 */
public enum MQConsumerStatus  implements BaseEnum {

    FAILURE("消费失败"),

    SUCCESS("消费成功") ;

    private final String key;

    private MQConsumerStatus(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

}
