package com.apec.framework.jpa.model;

import javax.persistence.MappedSuperclass;
import javax.persistence.Column;

/**
 * Title:
 *
 * @author yirde  2017/6/28.
 */
@MappedSuperclass
public abstract class BaseAuthenticatable extends BaseModel<Long> {

    /**
     * passphrase value
     */
    @Column(nullable = false, length = 40)
    protected String passphrase;

    /**
     * salt value in hex
     */
    @Column(nullable = false, length = 120)
    protected String salt;

    /**
     * 登录次数
     */
    @Column(nullable = false)
    protected Integer loginNumber;

    /**
     * indicate whether the entity need to change its password on login
     */
    @Column(nullable = false)
    protected Boolean needChangePassword;


    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getLoginNumber() {
        return loginNumber;
    }

    public void setLoginNumber(int loginNumber) {
        this.loginNumber = loginNumber;
    }


    public boolean isNeedChangePassword() {
        return needChangePassword;
    }

    public void setNeedChangePassword(boolean needChangePassword) {
        this.needChangePassword = needChangePassword;
    }
}
