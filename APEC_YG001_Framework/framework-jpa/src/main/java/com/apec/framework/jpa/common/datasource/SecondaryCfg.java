//package com.apec.framework.jpa.common.datasource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.persistence.EntityManager;
//import javax.sql.DataSource;
//import java.util.Map;
//
///**
// * 类 编 号：BL_PU1010202_SecondaryCfg
// * 类 名 称：PrimaryCfg
// * 内容摘要：次数据源配置
// * 完成日期：2016-07-14
// * 编码作者：
// */
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactorySecondary", transactionManagerRef = "transactionManagerSecondary", basePackages =
//{"com.apec.*.access.*.secondary.dao"})
//// 设置Repository所在位置
//public class SecondaryCfg
//{
//
//    @Autowired
//    @Qualifier("secondaryDataSource")
//    private DataSource secondaryDataSource;
//
//    @Bean(name = "entityManagerSecondary")
//    public EntityManager entityManager(EntityManagerFactoryBuilder builder)
//    {
//        return entityManagerFactorySecondary(builder).getObject().createEntityManager();
//    }
//
//    @Bean(name = "entityManagerFactorySecondary")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactorySecondary(EntityManagerFactoryBuilder builder)
//    {
//        return builder.dataSource(secondaryDataSource).properties(getVendorProperties(secondaryDataSource))
//            .packages("com.apec.*") // 设置实体类所在位置
//            .persistenceUnit("secondaryPersistenceUnit").build();
//    }
//
//    @Autowired
//    private JpaProperties jpaProperties;
//
//    private Map<String, String> getVendorProperties(DataSource dataSource)
//    {
//        return jpaProperties.getHibernateProperties(dataSource);
//    }
//
//    @Bean(name = "transactionManagerSecondary")
//    PlatformTransactionManager transactionManagerSecondary(EntityManagerFactoryBuilder builder)
//    {
//        return new JpaTransactionManager(entityManagerFactorySecondary(builder).getObject());
//    }
//
//}