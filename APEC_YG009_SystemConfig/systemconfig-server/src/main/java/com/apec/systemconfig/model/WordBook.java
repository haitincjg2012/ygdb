package com.apec.systemconfig.model;

import com.apec.framework.common.Constants;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 字典表（帮助查询）
 * Created by hmy on 2017/8/2.
 * @author hmy
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class WordBook extends BaseModel<Long> {

    private static final long serialVersionUID = 6677891523852240299L;

    /**
     * 编号
     */
    @Column(name = "code",nullable = false)
    private String code;

    /**
     * 数据字典值
     */
    @Column(name = "value")
    private String value;

    /**
     * 排序字段
     */
    @Column(name = "sort",nullable = false)
    private Integer sort;

    /**
     * 字典名称
     */
    @Column(name = "keyword",nullable = false)
    private String keyword;

}
