package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Title: 用户身份子类型
 *
 * @author yirde  2017/9/5.
 */
public enum UserDetailType  implements BaseEnum {

    DB_SG("收果代办"),
    DB_DG("调果代办"),
    LK_LB("老板"),
    LK_BG("保管");

    private final String key;

    @Override
    public String getKey(){ return key; }

    UserDetailType(final String key){
        this.key = key;
    }
}
