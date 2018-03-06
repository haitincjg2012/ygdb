package com.apec.cms.dao;

import com.apec.cms.model.Article;
import com.apec.cms.model.Channel;
import com.apec.framework.common.enumtype.CategoryType;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Title:
 *
 * @author yirde  2017/6/29.
 */
public interface ArticleDAO extends BaseDAO<Article, Long> {

    /**
     * 根据栏目查询记录
     * @param channel 栏目
     * @param categoryType 栏目分类
     * @param priority 是否显示
     * @param enableFlag 是否删除
     * @return List
     */
    List<Article> findByChannelAndCategoryAndPriorityAndEnableFlagOrderByOrdinal(Channel channel, CategoryType categoryType,
                                                                                 boolean priority, EnableFlag enableFlag);

    /**
     * 根据栏目查询行情
     * @param channelId channelId
     * @param enableFlag 状态码
     * @param nameFlag 是否需要通过名称查询
     * @param name 名称
     * @param beginDateFlag 是否需要通过开始时间查新
     * @param beginDate 开始时间
     * @param endDateFlag 是否需要通过结束时间查询
     * @param endDate 结束时间
     * @param personPubFlag 是否需要通过是个人发布还是系统发布查询
     * @param personPub 是否为个人发布
     * @param auditStateFlag 是否需要通过是否审核去查询
     * @param auditState 是否审核
     * @param notAuditStateFlag 是否为查询未审核
     * @param pageable 分页对象
     * @return 分页结果
     */
    @Query(value = "SELECT id,create_by,create_date,enable_flag,address,author,category,has_image,media,priv,pub_date,title,url,channel_id,ordinal,person_pub,audit_state,pass_date,content " +
                     "FROM cms_article c /**#pageable**/\n WHERE (FALSE = ?3 or  (author like %?4% or create_by in (select id from user where name like %?4%)) ) and (FALSE = ?9 or  person_pub = ?10 ) and (FALSE = ?11 or (true = ?13 or audit_state = ?12))  " +
            " and (FALSE = ?11 or (false = ?13 or id not in (select id from cms_article where audit_state != -1))) AND (FALSE = ?5 or  date(create_date) >= ?6 )  AND (FALSE = ?7 or  date(create_date) <= ?8 ) AND channel_id = ?1 AND enable_flag = ?2  ORDER BY audit_state ,priv desc ,create_date desc",
            countQuery = "SELECT COUNT(*) FROM cms_article WHERE (FALSE =" +
                    "?3 or (author like %?4% or create_by in (select id from user where name like %?4%)) ) and (FALSE = ?9 or  person_pub = ?10 ) and (FALSE = ?11 or (true = ?13 or audit_state = ?12)) " +
                    " and (FALSE = ?11 or (false = ?13 or id not in (select id from cms_article where audit_state != -1))) AND (FALSE = ?5 or  date(create_date) >= ?6 )  AND (FALSE = ?7 or  date(create_date) <= ?8 ) AND channel_id = ?1 AND enable_flag = ?2 " ,
            nativeQuery = true)
    Page<Object[]> queryByChannelAndEnableFlagOrderByCreateDate(String channelId, String enableFlag, boolean nameFlag, String name, boolean beginDateFlag, Date beginDate,
                                                             boolean endDateFlag, Date endDate,boolean personPubFlag,boolean personPub,boolean auditStateFlag,String auditState,boolean notAuditStateFlag, Pageable pageable);

    /**
     * 我的行情
     * @param channelId channelId
     * @param enableFlag 状态码
     * @param auditStateFlag 是否需要通过是否审核去查询
     * @param auditState 是否审核
     * @param notAuditStateFlag 是否为查询未审核
     * @param userId 用户id
     * @param pageable 分页对象
     * @return 分页结果
     */
    @Query(value = "SELECT id,create_by,create_date,enable_flag,address,author,category,has_image,media,priv,pub_date,title,url,channel_id,ordinal,person_pub,audit_state,pass_date,content " +
            " FROM cms_article c /**#pageable**/\n WHERE  (FALSE = ?3 or (true = ?5 or audit_state = ?4)) and (FALSE = ?3 or (false = ?5 or id not in (select id from cms_article where audit_state != -1))) " +
            " AND channel_id = ?1 AND enable_flag = ?2 and create_by = ?6 ORDER BY create_date desc",
            countQuery = "SELECT COUNT(*) FROM cms_article WHERE " +
                    " (FALSE = ?3 or (true = ?5 or audit_state = ?4)) and (FALSE = ?3 or (false = ?5 or id not in (select id from cms_article where audit_state != -1))) " +
                    "  AND channel_id = ?1 AND enable_flag = ?2 and create_by = ?6 " ,
            nativeQuery = true)
    Page<Object[]> queryMyNewsList(String channelId, String enableFlag,boolean auditStateFlag,String auditState,boolean notAuditStateFlag,String userId, Pageable pageable);

    /**
     * 根据栏目查询最新top n的有图片的新闻行情
     * @param channelId channelId
     * @param enableFlag 状态码
     * @param pageable 分页对象
     * @return 分页结果
     */
    @Query(value = "SELECT id,create_by,create_date,enable_flag,address,author,category,has_image,media,priv,pub_date,title,url,channel_id,ordinal,person_pub,audit_state,pass_date,content  " +
            "FROM cms_article c /**#pageable**/\n WHERE " +
            "channel_id = ?1 AND enable_flag = ?2  and audit_state = 'Y' AND url != -1 and has_image = 1 ORDER BY create_date desc",
            countQuery = "SELECT COUNT(*) FROM cms_article WHERE " +
                    "channel_id = ?1 AND enable_flag = ?2 and audit_state = 'Y'  AND url != -1 and has_image = 1 " ,
            nativeQuery = true)
    Page<Object[]> queryByChannelAndEnableFlagOrderByCreateDate(String channelId, String enableFlag, Pageable pageable);

    /**
     * 查询行情最大排序树加1
     * @param channelId channelId
     * @param enableFlag 状态码
     * @return 结果
     */
    @Query(value = "select max(ordinal)  FROM cms_article WHERE channel_id = ?1 AND enable_flag = ?2",
            nativeQuery = true)
    Object[] getMaxOrdinal(String channelId, String enableFlag);

    /**
     * 行情置顶
     * @param id 需要被置顶的行情
     * @param enableFlag 状态码
     * @return 改变的行数
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "update cms_article set priv = (select * from (SELECT max(priv) + 1 as r FROM cms_article) a) WHERE id = ?1 AND enable_flag = ?2",
            nativeQuery = true)
    int setStickArticle(Long id, String enableFlag);

    /**
     * 取消行情置顶
     * @param id 需要被置顶的行情
     * @param enableFlag 状态码
     * @return 改变的行数
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "update cms_article set priv = null WHERE id = ?1 AND enable_flag = ?2",
            nativeQuery = true)
    int closeStickArticle(Long id, String enableFlag);

}
