package com.apec.user.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.user.model.UserAuthRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 用户实名认证记录DAO
 * Created by hmy on 2017/7/17.
 */
public interface UserAuthRecordDAO extends BaseDAO<UserAuthRecord,Long> {

    Page<UserAuthRecord> findByUser_nameLikeAndEnableFlagOrderBySuccess(String userName, EnableFlag enableFlag, Pageable pageable);
}
