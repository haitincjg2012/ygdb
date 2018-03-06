package com.apec.society.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.mongodb.dao.BaseDAO;
import com.apec.society.model.SocietyLzlReply;

/**
 * Created by hmy on 2017/10/30.
 *
 * @author hmy
 */
public interface SocietyLzlReplyDAO extends BaseDAO<SocietyLzlReply,Long> {

    SocietyLzlReply findByIdAndEnableFlag(Long id, EnableFlag enableFlag);
}
