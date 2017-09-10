package com.apec.framework.dto;

import java.io.Serializable;

public class LogDTO implements Serializable
{

    private static final long serialVersionUID = 6340115760296173524L;

    private Long logId;
    
    private Long orderId;
    
    private Integer buyId;
    
    private Integer cityId;

    private Integer saleId;
    
    private String phone;
    
    public Integer getBuyId()
    {
        return buyId;
    }

    public void setBuyId(Integer buyId)
    {
        this.buyId = buyId;
    }

    public Integer getCityId()
    {
        return cityId;
    }

    public void setCityId(Integer cityId)
    {
        this.cityId = cityId;
    }

    public Integer getSaleId()
    {
        return saleId;
    }

    public void setSaleId(Integer saleId)
    {
        this.saleId = saleId;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public Long getLogId()
    {
        return logId;
    }

    public void setLogId(Long logId)
    {
        this.logId = logId;
    }
}
