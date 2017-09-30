package com.apec.goods.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.AttributeType;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * Title: 商品属性字典表
 *
 * @author yirde  2017/6/29.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class AttributeName extends BaseModel<Long> {

    private static final long serialVersionUID = 6232891566852240263L;

    /**
     * 商品属性名称
     */
    private String attrName;

    /**
     * 商品属性前缀
     */
    private String showPreFix;

    /**
     * 商品属性的后缀
     */
    private String showSuFix;

    /**
     * 远程请求地址
     */
    private String remoteUrlParam;

    /**
     * 备注
     */
    private String  remark;

    /**
     * 属性类型
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AttributeType attributeType = AttributeType.RADIO;


}
