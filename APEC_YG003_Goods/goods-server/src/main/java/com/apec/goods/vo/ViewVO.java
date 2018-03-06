package com.apec.goods.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by hmy on 2017/8/28.
 * @author hmy
 */
@Data
public class ViewVO {

    /**
     * 批量删除的对象id
     */
    private List<Long> ids;

}
