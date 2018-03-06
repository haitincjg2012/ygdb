package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Title:  Product
 *
 * @author yirde  2017/7/6.
 */
public enum ProductType implements BaseEnum {

    /**
     * 供应
     */
    SALE("供应"),

    /**
     * 求购
     */
    BUY("求购");

    private final String key;

    ProductType(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}