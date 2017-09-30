package com.apec.goods.model;

import javax.persistence.*;

import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import com.apec.framework.common.*;

import java.util.List;

/**
 * Title: 用户PO
 *
 * @author yirde  2017/3/21
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class Goods extends BaseModel<Long> {

    private static final long serialVersionUID = 6277891566852240333L;

    /**
     * 商品大类编码
     */
    @Column(length = 15)
    private String goodsCode;

    /**
     * 商品大类名称
     */
    private String goodsName;

    /**
     * 描述
     */
    private String remark;

    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 属性值
     */
    @OneToMany(mappedBy = "goods",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    private List<GoodsAttr> goodsAttrList;

    /**
     * 是否为主类
     */
    private boolean mainGoods;//如果为主类，则将其id存放于redis中

}
