/**
 * 
 */
package com.apec.framework.jpa.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 类 编 号：BL_PU6030202_BaseDAO
 * 类 名 称：BaseDAO
 * 内容摘要：基础JpaRepository类
 * 完成日期：2016-07-14
 * 编码作者：
 * @author xx
 */
@NoRepositoryBean
public interface BaseDAO<T, ID extends Serializable> extends JpaRepository<T, ID>,QueryDslPredicateExecutor<T>
{

}
