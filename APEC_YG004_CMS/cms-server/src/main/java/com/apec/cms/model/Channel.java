package com.apec.cms.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.CategoryType;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Title: 栏目
 *
 * @author yirde  2017/6/29.
 */
@Entity
@Data
@Table(name = "CMS_CHANNEL")
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class Channel extends BaseModel<Long>{

    private static final long serialVersionUID = 6377891566832250263L;

    /**
     * 栏目名
     */
    @Column(nullable = false)
    private String name;

    /**
     * 栏目类别
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryType category;

    /**
     * 描述
     */
    @Column
    private String description;

    /**
     * 编码
     */
    @Column(nullable = false,unique = true)
    private String code;


}
