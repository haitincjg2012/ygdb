package com.apec.society.dao;

import com.apec.framework.common.enums.Realm;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.mongodb.dao.BaseDAO;
import com.apec.society.model.SocietyPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * Created by hmy on 2017/10/30.
 *
 * @author hmy
 */
public interface SocietyPostDAO extends BaseDAO<SocietyPost,Long> {

    /**
     * 查询置顶值最大的数值
     * @param enableFlag 状态值
     * @return 帖子对象
     */
    SocietyPost findFirstByEnableFlagOrderByPrivDesc(EnableFlag enableFlag);

    /**
     * 按时间降序和数据来源查询帖子或行情，并且图片地址不为空
     * @param realm 数据来源
     * @param enableFlag 状态码
     * @param auditState 审核状态
     * @param pageable 分页对象
     * @return 分页结果
     */
    Page<SocietyPost> findByRealmAndEnableFlagAndAuditStateAndUrlNotNullOrderByCreateDateDesc(Realm realm ,EnableFlag enableFlag,String auditState, Pageable pageable);

    /**
     * 按时间降序和数据来源查询帖子
     * @param realm 数据来源
     * @param enableFlag 状态码
     * @return SocietyPost
     */
    List<SocietyPost> findByRealmAndEnableFlagOrderByCreateDateDesc(Realm realm ,EnableFlag enableFlag);

    /**
     * 统计开始时间和结束时间范围内的societyPost个数
     * @param realm 来源
     * @param enableFlag  状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计个数
     */
    List<SocietyPost> findByRealmAndEnableFlagAndStartTimeBeforeAndEndTimeAfterOrderByCreateDateDesc(Realm realm, EnableFlag enableFlag, Date startTime, Date endTime);

    /**
     * 统计开始时间和结束时间范围内的societyPost个数
     * @param realm 来源
     * @param enableFlag  状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计个数
     */
    Long countByRealmAndEnableFlagAndStartTimeBeforeAndEndTimeAfter(Realm realm,EnableFlag enableFlag, Date startTime,Date endTime);



}
