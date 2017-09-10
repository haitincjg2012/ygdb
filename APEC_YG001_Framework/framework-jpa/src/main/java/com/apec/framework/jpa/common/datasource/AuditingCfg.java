/**
 * 
 */
package com.apec.framework.jpa.common.datasource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.apec.framework.jpa.common.DbDateTimeProvider;

/**
 * @Description:获取当前时间
 *
 * @author huangcl
 *
 * @date 2016年7月14日
 *  
 */
@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
public class AuditingCfg
{

    @Bean
    public DbDateTimeProvider dateTimeProvider()
    {
        return new DbDateTimeProvider();
    }

}
