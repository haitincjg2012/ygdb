package com.apec.framework.dto;

import com.apec.framework.common.enumtype.EnableFlag;

import java.io.Serializable;
import java.util.Date;

/**
 * Title: 通用VO类
 *
 * @author yirde  2017/3/22
 */
public class BaseVO<PK extends Serializable> {

    private static final long serialVersionUID = -2477722490139509121L;

    private PK id;

    private EnableFlag enableFlag;

    private String createBy;

    private Date createDate;

    private String lastUpdateBy;

    private Date lastUpdateDate;

    public PK getId() {
        return id;
    }

    public void setId(PK id) {
        this.id = id;
    }


    public EnableFlag getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(EnableFlag enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Override
    public String toString(){
        return super.toString();
    }
}
