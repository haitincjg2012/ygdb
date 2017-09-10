package com.apec.framework.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonGetter;

/**
 * 类 编 号：
 * 类 名 称：PageDTO
 * 内容摘要：页面分页对象
 * 创建日期：2016-09-27 18:43
 * 编码作者：zhaolei
 */
public class PageDTO<T> implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 6268718986881793567L;

    private long totalElements;

    private int totalPages;

    private int number;

    private List<T> rows = new ArrayList<>();

    public PageDTO()
    {

    }

    public PageDTO(List<T> content, long totalElements, int totalPages, int number)
    {
        Assert.notNull( content, "Content must not be null!" );
        this.rows.addAll( content );
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.number = number;
    }

    public void build(long totalElements, PageRequest pageRequest, List<T> content)
    {
        this.rows.addAll( content );
        this.totalElements = totalElements;

        this.totalPages = pageRequest.getPageSize() == 0 ? 1 :
            (int)Math.ceil( (double)totalElements / (double)pageRequest.getPageSize() );
        this.number = pageRequest.getPageNumber() + 1;
    }

    public long getTotalElements()
    {
        return totalElements;
    }

    public void setTotalElements(long totalElements)
    {
        this.totalElements = totalElements;
    }

    @JSONField(name = "pageCount")
    @JsonGetter("pageCount")
    public int getTotalPages()
    {
        return totalPages;
    }

    public void setTotalPages(int totalPages)
    {
        this.totalPages = totalPages;
    }

    @JSONField(name = "currentNo")
    @JsonGetter("currentNo")
    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

    public List<T> getRows()
    {
        return rows;
    }

    public void setRows(List<T> rows)
    {
        this.rows = rows;
    }
}
