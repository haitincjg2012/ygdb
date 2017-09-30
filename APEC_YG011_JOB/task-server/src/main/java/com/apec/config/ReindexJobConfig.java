package com.apec.config;

import com.apec.framework.elasticjob.config.ElasticJobConfig;
import com.apec.framework.elasticjob.proxy.ZkRegistryCenterProxy;
import com.apec.framework.log.InjectLogger;
import com.apec.job.ReindexJob;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 重建索引job
 * Created by wubi on 2017/9/29.
 */
@Configuration
public class ReindexJobConfig extends ElasticJobConfig {
    @InjectLogger
    private Logger logger;

    @Value("${task.sysconfig.reindex}")
    private String reIndexJobConfigJsonStr;

    @Bean(name = "reIndexJob")
    public ReindexJob reIndexJob() {
        return new ReindexJob();
    }

    @Bean(initMethod = "init")
    public JobScheduler reIndexJobScheduler(@Qualifier("reIndexJob") ReindexJob reIndexJob, ObjectMapper objectMapper, @Qualifier("zkRegistryCenterProxy") ZkRegistryCenterProxy zkRegistryCenterProxy) throws Exception {
        logger.info("##########初始化Job:{}##########", reIndexJob.getClass().getName());
        return super.jobScheduler(reIndexJob, reIndexJobConfigJsonStr, objectMapper, zkRegistryCenterProxy);
    }

}
