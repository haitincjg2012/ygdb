package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Title:
 *
 * @author yirde  2017/7/20.
 */
public enum ImageDefType  implements BaseEnum {

    DEFAULT("默认图片"),
    SELF("自有图片");

    private final String key;

    private ImageDefType(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

}
