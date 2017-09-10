package com.apec.product.model;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.ImageDefType;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Title: ProductImage
 *
 * @author yirde  2017/7/6.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class ProductImage  extends BaseModel<Long> {

    private static final long serialVersionUID = 6277833211234240323L;

    /**
     * Product INFO
     */
    private Long  productId;

    /**
     * 图片地址
     */
    @Column(nullable = false)
    private String imageUrl;

    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 图片是否默认
     */
    private ImageDefType imageDefType;

}
