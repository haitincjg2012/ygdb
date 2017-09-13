package com.apec.cms.dao;

import com.apec.cms.model.Article;
import com.apec.cms.model.Channel;
import com.apec.framework.common.enumtype.CategoryType;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

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
    @Query(value = "SELECT id,create_by,create_date,enable_flag,address,author,category,has_image,media,priv,pub_date,title,url,channel_id,ordinal " +
                     "FROM cms_article /**#pageable**/\n WHERE channel_id = ?1 " +
                      "AND enable_flag = ?2  ORDER BY ordinal desc",
            countQuery = "SELECT COUNT(*) FROM cms_article WHERE channel_id = ?1 " +
                    "AND enable_flag = ?2 ",
            nativeQuery = true)
    Page<Object[]> queryByChannelAndEnableFlagOrderByOrdinal(String channelId, String enableFlag, Pageable pageable);

    /**
     * 查询行情最大排序树加1
     * @param channelId
     * @param enableFlag
     * @return
     */
    @Query(value = "select max(ordinal)  FROM cms_article WHERE channel_id = ?1 AND enable_flag = ?2",
            nativeQuery = true)
    Object[] getMaxOrdinal(String channelId, String enableFlag);

}
