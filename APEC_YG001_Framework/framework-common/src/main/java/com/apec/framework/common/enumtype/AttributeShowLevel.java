package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Title: 商品属性显示等级
 *
 * @author yirde  2017/6/29.
 */
public enum AttributeShowLevel  implements BaseEnum {

    /**
     * 首要显示
     */
    FIRST("首要显示"),

    /**
     * 次要显示
     */
    SECOND("次要显示"),

    /**
     * 远程地址访问
     */
    OTHER("其它");

    private final String key;

    AttributeShowLevel(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
