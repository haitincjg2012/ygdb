package com.apec.user.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.user.model.UserPointRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 用户的积分记录
 * Created by hmy on 2017/7/6.
 */
public interface UserPointRecordDAO extends BaseDAO<UserPointRecord,Long> {

    Page<UserPointRecord> findByUserIdAndEnableFlag(Long userId, EnableFlag enableFlag, Pageable pageable);

}
