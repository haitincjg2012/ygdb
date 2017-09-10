/**
 * 
 */
package com.apec.framework.jpa.common.datasource;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 类 编 号：BL_PU1010202_DataSourceCfg
 * 类 名 称：DataSourceCfg
 * 内容摘要：多数据源配置
 * 完成日期：2016-07-14
 * 编码作者：
 */
@Configuration
public class DataSourceCfg
{

    /**
     * 主数据源
     */
    @Bean(name = "primaryDataSource")
    @Qualifier("primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource()
    {
        return DataSourceBuilder.create().build();
    }

    /**
     * 次数据源
     */
//    @Bean(name = "secondaryDataSource")
//    @Qualifier("secondaryDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.secondary")
//    @Primary
//    public DataSource secondaryDataSource()
//    {
//        return DataSourceBuilder.create().build();
//    }

}