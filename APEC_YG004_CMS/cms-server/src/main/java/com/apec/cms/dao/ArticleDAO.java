package com.apec.cms.dao;

import com.apec.cms.model.Article;
import com.apec.cms.model.Channel;
import com.apec.framework.common.enumtype.CategoryType;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.jpa.dao.BaseDAO;

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

}
