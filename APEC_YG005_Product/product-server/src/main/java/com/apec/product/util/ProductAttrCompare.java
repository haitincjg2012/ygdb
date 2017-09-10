package com.apec.product.util;

import com.apec.product.vo.ProductAttrVO;

import java.util.Comparator;

/**
 * Title: 对排序字段进行排序
 *
 * @author yirde  2017/7/20.
 */
public class ProductAttrCompare  implements Comparator<ProductAttrVO> {

    @Override
    public int compare(ProductAttrVO o1, ProductAttrVO o2) {
        if(o1.getSort() > o2.getSort()){
            return 1;
        }
        if(o1.getSort() < o2.getSort()){
            return -1;
        }

        return 0;
    }
}
