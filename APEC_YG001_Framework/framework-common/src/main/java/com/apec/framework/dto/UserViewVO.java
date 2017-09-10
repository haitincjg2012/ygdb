package com.apec.framework.dto;


/**
 * Title:
 *
 * @author yirde  2017/7/29.
 */
public class UserViewVO {

    /**
     * 该用户总的可用积分
     */
    private Integer point;

    /**
     *  用户积分等级
     */
    private String userLevelKey ;

    private String userLevelName;

    /**
     * 用户实名状态
     */
    private String  userRealAuthKey;

    private String userRealAuthName;

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getUserLevelKey() {
        return userLevelKey;
    }

    public void setUserLevelKey(String userLevelKey) {
        this.userLevelKey = userLevelKey;
    }

    public String getUserLevelName() {
        return userLevelName;
    }

    public void setUserLevelName(String userLevelName) {
        this.userLevelName = userLevelName;
    }

    public String getUserRealAuthKey() {
        return userRealAuthKey;
    }

    public void setUserRealAuthKey(String userRealAuthKey) {
        this.userRealAuthKey = userRealAuthKey;
    }

    public String getUserRealAuthName() {
        return userRealAuthName;
    }

    public void setUserRealAuthName(String userRealAuthName) {
        this.userRealAuthName = userRealAuthName;
    }
}
