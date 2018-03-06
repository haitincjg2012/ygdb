package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Title: 商品属性类型
 *
 * @author yirde  2017/6/29.
 */
public enum AttributeType  implements BaseEnum {

    /**
     * 单选
     */
    RADIO("单选"),

    /**
     * 多选
     */
    MULTI("多选"),

    /**
     * 下拉
     */
    SELECT("下拉"),

    /**
     * 文本域输入
     */
    TEXTAREA("文本域输入"),

    /**
     *  //远程地址访问
     */
     REMOTE("远程访问");

    private final String key;

    AttributeType(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
