package com.apec.framework.mongodb.vo;

import com.apec.framework.common.enumtype.EnableFlag;

import java.io.Serializable;
import java.util.Date;

/**
 * Title:
 *
 * @author yirde  2017/10/20.
 */
public class BaseVO<PK extends Serializable> {

    private static final long serialVersionUID = -2477722490139503211L;

    /**
     * ID
     */
    private Long id;

    /**
     * 状态控制
     */
    private EnableFlag enableFlag;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 修改时间
     */
    private Date lastUpdateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnableFlag getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(EnableFlag enableFlag) {
        this.enableFlag = enableFlag;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
