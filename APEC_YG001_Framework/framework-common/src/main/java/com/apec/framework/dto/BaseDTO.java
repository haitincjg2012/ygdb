package com.apec.framework.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 类 编 号：
 * 类 名 称：BaseDTO
 * 内容摘要：抽象出公用属性
 * 创建日期：2016/10/25
 * 编码作者：
 */
public class BaseDTO
{
    /**
     * 分页数
     */
    private int pageNumber;
    
    private int currentNo;

    /**
     * 分页大小
     */
    private int pageSize;

    /**
     * 排序类型
     */
    private String sortType;

    /**
     * 获取 分页数
     */
    @JsonIgnore
    public int getPageNumber()
    {
        return this.pageNumber;
    }

    /**
     * 设置 分页数
     */
    public void setPageNumber(int pageNumber)
    {
        this.pageNumber = pageNumber;
        this.currentNo = pageNumber;
    }

    /**
     * 获取 分页大小
     */
    @JsonIgnore
    public int getPageSize()
    {
        return this.pageSize;
    }

    /**
     * 设置 分页大小
     */
    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    /**
     * 获取 排序类型
     */
    @JsonIgnore
    public String getSortType()
    {
        return this.sortType;
    }

    /**
     * 设置 排序类型
     */
    public void setSortType(String sortType)
    {
        this.sortType = sortType;
    }

    @JsonIgnore
    public int getCurrentNo()
    {
        return currentNo;
    }

    public void setCurrentNo(int currentNo)
    {
        this.currentNo = currentNo;
        this.pageNumber= currentNo;
    }
}
