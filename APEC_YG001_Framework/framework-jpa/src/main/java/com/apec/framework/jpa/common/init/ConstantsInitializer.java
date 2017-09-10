package com.apec.framework.jpa.common.init;

import com.apec.framework.common.tools.IDGenerator;
import com.apec.framework.jpa.common.DbDateTimeProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;

import javax.annotation.PostConstruct;

/**
 * 类 编 号：
 * 类 名 称：ConstantsInitializer
 * 内容摘要：初始化静态变量
 * 创建日期：2016/10/19
 * 编码作者：
 */
@Configuration
public class ConstantsInitializer
{
    @Value("${workerId}")
    private long workerId;

    @PostConstruct
    public void initConstants()
    {
        IDGenerator.setWorkerId( workerId );
    }

    @Bean(name = "dateTimeProvider")
    public DateTimeProvider dateTimeProvider()
    {
        return new DbDateTimeProvider();
    }
}
