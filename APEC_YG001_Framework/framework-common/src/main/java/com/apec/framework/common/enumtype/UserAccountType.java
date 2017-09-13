package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 *
 * Title: 用户账户类型
 *
 * @author yirde  2017/9/7.
 */
public enum UserAccountType implements BaseEnum {

    ORG_ACCOUNT("组织账户"),
    IND_ACCOUNT("个体账户"),
    ORG_MAIN_ACCOUNT("组织账户主体账号"),
    ORG_CHILD_ACCOUNT("组织账户子级账号"),
    IND_MAIN_ACCOUNT("个体账号");

    private final String key;

    private UserAccountType(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

}
