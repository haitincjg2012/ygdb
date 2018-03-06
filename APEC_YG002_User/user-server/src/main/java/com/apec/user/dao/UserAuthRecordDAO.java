package com.apec.user.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;
import com.apec.user.model.UserAuthRecord;

/**
 * 用户实名认证记录DAO
 * Created by hmy on 2017/7/17.
 * @author xxx
 */
public interface UserAuthRecordDAO extends BaseDAO<UserAuthRecord,Long> {

    /**
     * 实名认证记录查询
     * @param id id
     * @param enableFlag enableFlag
     * @return UserAuthRecord
     */
    UserAuthRecord findByIdAndEnableFlag(Long id,EnableFlag enableFlag);

}
