package com.apec.framework.elasticjob.config;

import com.apec.framework.elasticjob.listeners.SimpleListener;
import com.apec.framework.elasticjob.proxy.ZkRegistryCenterProxy;
import com.apec.framework.elasticjob.schedule.SpringJobScheduler;
import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

//import com.apec.framework.elasticjob.listeners.SimpleDistributeListener;

/**
 * Elastic-job作业配置类
 * 
 * @author gjp
 *
 */
@Configuration
public class ElasticJobConfig
{

    private static Logger logger = LoggerFactory.getLogger(ElasticJobConfig.class);

    @Bean
    public ObjectMapper objectMapper()
    {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }

    /**
     * 初始化elastic-job配置
     * @param jobClassName 应用job类名
     * @param jobConfigJsonStr 应用job配置参数
     * @param objectMapper  json转换对象
     * @param zkRegistryCenterProxy zk配置
     * @return
     * @throws Exception
     */
    public JobScheduler jobScheduler(ElasticJob jobObject, String jobConfigJsonStr, ObjectMapper objectMapper,
        ZkRegistryCenterProxy zkRegistryCenterProxy) throws Exception
    {
        if(StringUtils.isBlank(jobConfigJsonStr) || StringUtils.isBlank(jobConfigJsonStr))
        {
            logger.error(" *** parameter is empty jobClassName=" + jobObject.getClass().getName() + ",jobConfigJsonStr="
                         + jobConfigJsonStr);
            return null;
        }
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        Class<?> jobClass = classLoader.loadClass(jobClassName);
//        ElasticJob jobObject = (ElasticJob)jobClass.newInstance();
        SimpleJobConfigItems simpleJobConfigItems = null;
        try
        {
            simpleJobConfigItems = objectMapper.readValue(jobConfigJsonStr, SimpleJobConfigItems.class);
        }
        catch (JsonParseException e)
        {
            logger.error(" *** JsonParse Exception e=" + e);
            return null;
        }
        catch (JsonMappingException e)
        {
            logger.error(" *** JsonMapping Exception e=" + e);
            return null;
        }
        catch (IOException e)
        {
            logger.error(" *** Json Exception e=" + e);
            return null;
        }
        
        ElasticJobListener listens[] = {new SimpleListener()};
//        ElasticJobListener listens[] =
//        {new SimpleListener(), new SimpleDistributeListener(1000L, 2000L)};
        LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(new SimpleJobConfiguration(
            JobCoreConfiguration
                .newBuilder(simpleJobConfigItems.getJobName(), simpleJobConfigItems.getCron(),
                            simpleJobConfigItems.getShardingTotalCount())
                .shardingItemParameters(simpleJobConfigItems.getShardingItemParameters())
                .failover(simpleJobConfigItems.isFailover()).build(),//新增失效转移参数设置
                jobObject.getClass().getCanonicalName())).overwrite(true).build(); //覆盖注册中心的配置
        return new SpringJobScheduler(jobObject, zkRegistryCenterProxy, liteJobConfiguration, listens);
    }

    @Value("${ejzk.json}")
    private String EjzkConfigJsonStr;

    // 连接zk的配置信息，可共用一个zk
    @Bean(name = "ejZookeeperConfig")
    public EJZKconfigItem ejZookeeperConfig(ObjectMapper objectMapper)
    {
        try
        {
            return objectMapper.readValue(EjzkConfigJsonStr, EJZKconfigItem.class);
        }
        catch (JsonParseException e)
        {
            logger.error(" *** JsonParse Exception e=" + e);
        }
        catch (JsonMappingException e)
        {
            logger.error(" *** JsonMapping Exception e=" + e);
        }
        catch (IOException e)
        {
            logger.error(" *** Json Exception e=" + e);
        }
        return null;
    }

    // 连接zk的配置信息，可共用一个zk
    @Bean(name = "zkRegistryCenterProxy")
    public ZkRegistryCenterProxy zkRegistryCenterProxy(@Qualifier("ejZookeeperConfig") EJZKconfigItem ejZookeeperConfig)
    {
        ZookeeperConfiguration zkConfig = new ZookeeperConfiguration(ejZookeeperConfig.getServerLists(),
            ejZookeeperConfig.getNamespace());
        zkConfig.setSessionTimeoutMilliseconds(60000);
        zkConfig.setConnectionTimeoutMilliseconds(15000);
        ZkRegistryCenterProxy zkRegistryCenterProxy = new ZkRegistryCenterProxy(zkConfig);
        return zkRegistryCenterProxy;
    }

}