package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Title: 商品属性显示等级
 *
 * @author yirde  2017/6/29.
 */
public enum AttributeShowLevel  implements BaseEnum {

    FIRST("首要显示"),

    SECOND("次要显示"),

    OTHER("其它"); //远程地址访问

    private final String key;

    private AttributeShowLevel(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
