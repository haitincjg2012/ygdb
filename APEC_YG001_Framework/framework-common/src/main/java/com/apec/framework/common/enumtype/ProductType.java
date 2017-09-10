package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Title:  Product
 *
 * @author yirde  2017/7/6.
 */
public enum ProductType implements BaseEnum {

    SALE("供应"),

    BUY("求购");

    private final String key;

    private ProductType(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}