package com.apec.society.dao;

import com.apec.framework.mongodb.dao.BaseDAO;
import com.apec.society.model.SocietyInfo;
import org.springframework.stereotype.Repository;

/**
 * Title: SocietyInfo DAO
 *
 * @author yirde  2017/10/20.
 */
@Repository
public interface SocietyInfoDAO extends BaseDAO<SocietyInfo, Long> {

}
