package com.apec.systemconfig.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.enumtype.SearchType;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.systemconfig.model.PreSearch;

import java.util.List;

/**
 * Created by hmy on 2017/7/31.
 */
public interface PreSearchDAO extends BaseDAO<PreSearch,Long> {

    /**
     *
     * @param searchType
     * @param enableFlag
     * @return
     */
    List<PreSearch> findBySearchTypeAndEnableFlag(SearchType searchType, EnableFlag enableFlag);

    PreSearch findBySearchTypeAndEnableFlagAndSort(SearchType searchType, EnableFlag enableFlag,Integer sort);

    Integer countBySearchTypeAndEnableFlag(SearchType searchType, EnableFlag enableFlag);

}
