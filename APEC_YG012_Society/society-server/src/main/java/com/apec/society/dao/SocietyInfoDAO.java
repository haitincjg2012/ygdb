package com.apec.society.dao;

import com.apec.framework.mongodb.dao.BaseDAO;
import com.apec.society.model.SocietyInfo;
import org.springframework.data.mongodb.repository.Query;

/**
 * Title: SocietyInfo DAO
 *
 * @author yirde  2017/10/20.
 */
public interface SocietyInfoDAO extends BaseDAO<SocietyInfo, Long> {

     /**
      * 通过id查找圈子
      *
      * @param id id
      * @return SocietyInfo
      */
     @Query(value = "{id:?1}")
     SocietyInfo findById(Long id);
}
