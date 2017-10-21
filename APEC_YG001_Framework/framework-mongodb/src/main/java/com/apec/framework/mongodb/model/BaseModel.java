package com.apec.framework.mongodb.model;

import com.apec.framework.common.enumtype.EnableFlag;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * Title: Model Base
 *
 * @author yirde  2017/10/19.
 */
@Document
public class BaseModel <PK extends Serializable> implements Persistable<PK>{

    private static final long serialVersionUID = -2327722490039509121L;

    /**
     *  主键
     */
    @Id
    private PK id;

    /**
     * 状态控制
     */
    private EnableFlag enableFlag = EnableFlag.Y;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 修改时间
     */
    private Date lastUpdateDate;

    public void setId(PK id) {
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

    @Override
    public PK getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return null == this.id;
    }
}
