package com.apec.product.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.UserType;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * Title:
 *
 * @author yirde  2017/7/20.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_sku_info")
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class ProductSkuInfo extends BaseModel<Long> {

    private static final long serialVersionUID = 6277831566343240323L;

    private Long userId;

    /**
     * 发布人的姓名
     */
    private String userName;

    private String mobile;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType;


    private String skuName;

    /**
     * 需求 属性
     */
    @OneToMany(mappedBy = "productInfo",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ProductAttr> productAttrs;

}
