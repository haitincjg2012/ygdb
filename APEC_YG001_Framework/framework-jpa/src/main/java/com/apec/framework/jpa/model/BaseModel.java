package com.apec.framework.jpa.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.EnableFlag;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * 类 编 号：BL_PU6030202_BaseModel
 * 类 名 称：BaseModel
 * 内容摘要：框架基本实体类,所有的实体需集成该类
 * 完成日期：2016-07-14
 * 编码作者：
 */
@MappedSuperclass
@EntityListeners(
{AuditingEntityListener.class})
public class BaseModel<PK extends Serializable> implements Persistable<PK>
{

    private static final long serialVersionUID = -2477722490039509121L;

    @Id
    @GeneratedValue(generator = Constants.SYSTEM_GENERATOR)
    @Column(name = "ID")
    private PK id;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENABLE_FLAG")
    private EnableFlag enableFlag = EnableFlag.Y;

    @Column(name = "CREATE_BY", updatable = false)
    private String createBy;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone="GMT+8")
    @Column(name = "CREATE_DATE", updatable = false)
    private Date createDate;

    @Column(name = "LAST_UPDATE_BY")
    private String lastUpdateBy;

    @Column(name = "LAST_UPDATE_DATE")
    @LastModifiedDate
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;

    public void setId(PK id)
    {
        this.id = id;
    }

    @Override
    public PK getId()
    {
        // TODO Auto-generated method stub
        return id;
    }

    public EnableFlag getEnableFlag()
    {
        return enableFlag;
    }

    public void setEnableFlag(EnableFlag enableFlag)
    {
        this.enableFlag = enableFlag;
    }

    /**
     * @return the createBy
     */
    public String getCreateBy()
    {
        return createBy;
    }

    /**
     * @param createBy
     *            the createBy to set
     */
    public void setCreateBy(String createBy)
    {
        this.createBy = createBy;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate()
    {
        return createDate;
    }

    /**
     * @param createDate
     *            the createDate to set
     */
    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate;
    }

    /**
     * @return the lastUpdateBy
     */
    public String getLastUpdateBy()
    {
        return lastUpdateBy;
    }

    /**
     * @param lastUpdateBy
     *            the lastUpdateBy to set
     */
    public void setLastUpdateBy(String lastUpdateBy)
    {
        this.lastUpdateBy = lastUpdateBy;
    }

    /**
     * @return the lastUpdateDate
     */
    public Date getLastUpdateDate()
    {
        return lastUpdateDate;
    }

    /**
     * @param lastUpdateDate
     *            the lastUpdateDate to set
     */
    public void setLastUpdateDate(Date lastUpdateDate)
    {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Override
    @JsonIgnore
    public boolean isNew()
    {
        return null == this.id;
    }

}
