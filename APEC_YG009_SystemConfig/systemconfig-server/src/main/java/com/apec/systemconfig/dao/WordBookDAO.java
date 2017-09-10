package com.apec.systemconfig.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.systemconfig.model.WordBook;

import java.util.List;

/**
 * Created by hmy on 2017/8/2.
 */
public interface WordBookDAO extends BaseDAO<WordBook,Long>{

    /**
     * 根据code和enableflag查询相应的字典表数据
     * @param code
     * @param enableFlag
     * @return
     */
    List<WordBook> findByCodeAndEnableFlagOrderBySort(String code, EnableFlag enableFlag);

    /**
     * 查询所有的wordBook数据
     * @param enableFlag
     * @return
     */
    List<WordBook> findByEnableFlagOrderBySort(EnableFlag enableFlag);

}
