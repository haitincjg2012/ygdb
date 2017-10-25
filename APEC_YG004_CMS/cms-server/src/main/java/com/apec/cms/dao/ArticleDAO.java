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
     * @param channelId
     * @param enableFlag
     * @return
     */
    @Query(value = "SELECT id,create_by,create_date,enable_flag,address,if(person_pub,(select name from user where id = c.create_by),author),category,has_image,media,priv,pub_date,title,url,channel_id,ordinal,person_pub,audit_state,pass_date " +
                     "FROM cms_article c /**#pageable**/\n WHERE (FALSE = ?3 or  (author like %?4% or create_by in (select id from user where name like %?4%)) ) and (FALSE = ?9 or  person_pub = ?10 ) and (FALSE = ?11 or (true = ?13 or audit_state = ?12))  " +
            " and (FALSE = ?11 or (false = ?13 or id not in (select id from cms_article where audit_state != -1))) AND (FALSE = ?5 or  date(create_date) >= ?6 )  AND (FALSE = ?7 or  date(create_date) <= ?8 ) AND channel_id = ?1 AND enable_flag = ?2  ORDER BY audit_state ,priv desc ,create_date desc",
            countQuery = "SELECT COUNT(*) FROM cms_article WHERE (FALSE =" +
                    "?3 or (author like %?4% or create_by in (select id from user where name like %?4%)) ) and (FALSE = ?9 or  person_pub = ?10 ) and (FALSE = ?11 or (true = ?13 or audit_state = ?12)) " +
                    " and (FALSE = ?11 or (false = ?13 or id not in (select id from cms_article where audit_state != -1))) AND (FALSE = ?5 or  date(create_date) >= ?6 )  AND (FALSE = ?7 or  date(create_date) <= ?8 ) AND channel_id = ?1 AND enable_flag = ?2 " ,
            nativeQuery = true)
    Page<Object[]> queryByChannelAndEnableFlagOrderByCreateDate(String channelId, String enableFlag, boolean nameFlag, String name, boolean beginDateFlag, Date beginDate,
                                                             boolean endDateFlag, Date endDate,boolean personPubFlag,boolean personPub,boolean auditStateFlag,String auditState,boolean notAuditStateFlag, Pageable pageable);

    /**
     * 根据栏目查询行情（前端）
     * @param channelId
     * @param enableFlag
     * @return
     */
    @Query(value = "SELECT id,create_by,create_date,enable_flag,address,if(person_pub,(select name from user where id = c.create_by),author),category,has_image,media,priv,pub_date,title,url,channel_id,ordinal,person_pub,audit_state,pass_date " +
            " FROM cms_article c /**#pageable**/\n WHERE  (FALSE = ?3 or (true = ?5 or audit_state = ?4)) and (FALSE = ?3 or (false = ?5 or id not in (select id from cms_article where audit_state != -1))) " +
            " AND channel_id = ?1 AND enable_flag = ?2 and create_by = ?6 ORDER BY create_date desc",
            countQuery = "SELECT COUNT(*) FROM cms_article WHERE " +
                    " (FALSE = ?3 or (true = ?5 or audit_state = ?4)) and (FALSE = ?3 or (false = ?5 or id not in (select id from cms_article where audit_state != -1))) " +
                    "  AND channel_id = ?1 AND enable_flag = ?2 and create_by = ?6 " ,
            nativeQuery = true)
    Page<Object[]> queryMyNewsList(String channelId, String enableFlag,boolean auditStateFlag,String auditState,boolean notAuditStateFlag,String userId, Pageable pageable);

    /**
     * 根据栏目查询最新top n的有图片的新闻行情
     * @param channelId
     * @param enableFlag
     * @return
     */
    @Query(value = "SELECT id,create_by,create_date,enable_flag,address,if(person_pub,(select name from user where id = c.create_by),author),category,has_image,media,priv,pub_date,title,url,channel_id,ordinal,person_pub,audit_state,pass_date  " +
            "FROM cms_article c /**#pageable**/\n WHERE " +
            "channel_id = ?1 AND enable_flag = ?2  and audit_state = 'Y' AND url != -1 and has_image = 1 ORDER BY priv desc ,create_date desc",
            countQuery = "SELECT COUNT(*) FROM cms_article WHERE " +
                    "channel_id = ?1 AND enable_flag = ?2 and audit_state = 'Y'  AND url != -1 and has_image = 1 " ,
            nativeQuery = true)
    Page<Object[]> queryByChannelAndEnableFlagOrderByCreateDate(String channelId, String enableFlag, Pageable pageable);

    /**
     * 查询行情最大排序树加1
     * @param channelId
     * @param enableFlag
     * @return
     */
    @Query(value = "select max(ordinal)  FROM cms_article WHERE channel_id = ?1 AND enable_flag = ?2",
            nativeQuery = true)
    Object[] getMaxOrdinal(String channelId, String enableFlag);

    /**
     * 查询行情具体信息
     * @param id
     * @param enableFlag
     * @return
     */
    @Query(value = "SELECT id,create_by,create_date,enable_flag,address,if(person_pub,(select name from user where id = c.create_by),author) as author,category,has_image,media,priv,pub_date,title,url,channel_id,ordinal,person_pub,audit_state,pass_date,last_update_by,last_update_date,content,news_id,priority " +
            "FROM cms_article c WHERE id = ?1 and enable_flag = ?2",nativeQuery = true)
    Article queryArticleById(Long id, String enableFlag);

    /**
     * 行情置顶
     * @param id
     * @param enableFlag
     * @return
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "update cms_article set priv = (select * from (SELECT max(priv) + 1 as r FROM cms_article) a) WHERE id = ?1 AND enable_flag = ?2",
            nativeQuery = true)
    int setStickArticle(Long id, String enableFlag);

    /**
     * 行情置顶
     * @param id
     * @param enableFlag
     * @return
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "update cms_article set priv = '' WHERE id = ?1 AND enable_flag = ?2",
            nativeQuery = true)
    int closeStickArticle(Long id, String enableFlag);

}
