package com.apec.systemconfig.dao;

import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.systemconfig.model.FeedBack;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by hmy on 2017/8/28.
 */
public interface FeedBackDAO extends BaseDAO<FeedBack,Long> {

    /**
     * 批量删除goods
     * @param ids
     * @return
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "update feed_back set enable_flag = 'N',last_update_date = now(),last_update_by = :userId where id in :ids and enable_flag = 'Y'",nativeQuery = true)
    int deleteFeedBackList(@Param("ids") List<Long> ids, @Param("userId") String userId);


}
