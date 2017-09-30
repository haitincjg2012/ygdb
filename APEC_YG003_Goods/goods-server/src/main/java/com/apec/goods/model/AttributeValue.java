package com.apec.goods.model;

import com.apec.framework.common.Constants;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Title: 商品属性值
 *
 * @author yirde  2017/6/29.
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class AttributeValue extends BaseModel<Long> {

    private static final long serialVersionUID = 6232891566822240263L;
    /**
     * 属性名
     */
    private Long  attributeNameId;

    /**
     * 商品属性名称
     */
    private String attrValue;

    /**
     * 排序
     */
    private Integer sort;

}
