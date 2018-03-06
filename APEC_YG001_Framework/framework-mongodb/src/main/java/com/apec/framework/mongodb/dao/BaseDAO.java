package com.apec.framework.mongodb.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Title:Mongodb Repository
 *
 * @author yirde  2017/10/20.
 */
@NoRepositoryBean
public interface BaseDAO <T, ID extends Serializable> extends MongoRepository<T, ID>,QueryDslPredicateExecutor<T> {
}
