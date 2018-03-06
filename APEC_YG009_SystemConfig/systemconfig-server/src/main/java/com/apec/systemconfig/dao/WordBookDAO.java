package com.apec.systemconfig.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.systemconfig.model.WordBook;

import java.util.List;

/**
 * Created by hmy on 2017/8/2.
 * @author hmy
 */
public interface WordBookDAO extends BaseDAO<WordBook,Long>{

    /**
     * 根据code和enableflag查询相应的字典表数据
     * @param code 编码
     * @param enableFlag 状态码
     * @return 字典表数据
     */
    List<WordBook> findByCodeAndEnableFlagOrderBySort(String code, EnableFlag enableFlag);


}
