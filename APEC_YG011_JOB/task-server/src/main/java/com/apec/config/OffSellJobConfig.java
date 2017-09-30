package com.apec.config;

import com.apec.framework.elasticjob.config.ElasticJobConfig;
import com.apec.framework.elasticjob.proxy.ZkRegistryCenterProxy;
import com.apec.framework.log.InjectLogger;
import com.apec.job.OffSellJob;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OffSellJobConfig extends ElasticJobConfig
{

    @InjectLogger
    private Logger logger;

   // **************product-offSell配置开始********************

    @Value("${task.product.off.sell}")
    private String offSellJobConfigJsonStr;

    @Bean(name = "offSellJob")
    public OffSellJob offSellJob() {
        return new OffSellJob();
    }

    @Bean(initMethod = "init")
    public JobScheduler offSellJobScheduler(@Qualifier("offSellJob") OffSellJob offSellJob, ObjectMapper objectMapper, @Qualifier("zkRegistryCenterProxy") ZkRegistryCenterProxy zkRegistryCenterProxy) throws Exception
    {
        logger.info("##########初始化Job:{}##########", offSellJob.getClass().getName());
        return super.jobScheduler(offSellJob, offSellJobConfigJsonStr, objectMapper, zkRegistryCenterProxy);
    }

    // **************product-offSell配置结束********************





}
