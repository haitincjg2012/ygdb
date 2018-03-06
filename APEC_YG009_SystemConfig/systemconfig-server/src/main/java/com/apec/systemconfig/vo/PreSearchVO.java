package com.apec.systemconfig.vo;

import com.apec.framework.common.enumtype.SearchType;
import lombok.Data;

/**
 * Created by hmy on 2017/8/1.
 * @author hmy
 */
@Data
public class PreSearchVO {

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 搜索类型
     */
    private SearchType searchType;


}
