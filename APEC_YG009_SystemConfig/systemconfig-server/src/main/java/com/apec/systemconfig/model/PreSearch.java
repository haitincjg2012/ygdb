package com.apec.systemconfig.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.SearchType;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Created by hmy on 2017/7/31.
 * 预制搜索表（热门搜索/预制搜索）
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class PreSearch extends BaseModel<Long> {

    private static final long serialVersionUID = 6677891523852240269L;

    /**
     * 关键字
     */
    @Column(name = "keyword",nullable = false)
    private String keyword;

    /**
     * 搜索类型
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SearchType searchType;

    /**
     * 排序字段
     */
    @Column(name = "sort")
    private Integer sort;


}
