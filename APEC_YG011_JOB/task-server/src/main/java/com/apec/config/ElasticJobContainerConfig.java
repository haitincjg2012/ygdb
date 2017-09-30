package com.apec.config;

import com.apec.framework.elasticjob.config.ElasticJobConfig;
import com.apec.framework.elasticjob.proxy.ZkRegistryCenterProxy;
import com.apec.framework.log.InjectLogger;
import com.apec.job.OffSellJob;
import com.apec.job.PerfectUserPoint;
import com.apec.job.ReindexJob;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticJobContainerConfig extends ElasticJobConfig {

    @InjectLogger
    private Logger logger;

    //**************product-offSell配置开始********************

    @Value("${task.product.off.sell}")
    private String offSellJobConfigJsonStr;

    @Bean(name = "offSellJob")
    public OffSellJob offSellJob() {
        return new OffSellJob();
    }

    @Bean(initMethod = "init")
    public JobScheduler offSellJobScheduler(@Qualifier("offSellJob") OffSellJob offSellJob, ObjectMapper objectMapper, @Qualifier("zkRegistryCenterProxy") ZkRegistryCenterProxy zkRegistryCenterProxy) throws Exception {
        logger.info("##########初始化Job:{}##########", offSellJob.getClass().getName());
        return super.jobScheduler(offSellJob, offSellJobConfigJsonStr, objectMapper, zkRegistryCenterProxy);
    }

    // **************product-offSell配置结束********************

    // **************sysconfig-reindex配置开始********************

    /*@Value("${task.sysconfig.reindex}")
    private String reIndexJobConfigJsonStr;

    @Bean(name = "reIndexJob")
    public ReindexJob reIndexJob() {
        return new ReindexJob();
    }

    @Bean(initMethod = "init")
    public JobScheduler reIndexJobScheduler(@Qualifier("reIndexJob") ReindexJob reIndexJob, ObjectMapper objectMapper, @Qualifier("zkRegistryCenterProxy") ZkRegistryCenterProxy zkRegistryCenterProxy) throws Exception {
        logger.info("##########初始化Job:{}##########", reIndexJob.getClass().getName());
        return super.jobScheduler(reIndexJob, reIndexJobConfigJsonStr, objectMapper, zkRegistryCenterProxy);
    }*/

    // **************sysconfig-reindex配置结束********************


    /***************countVoucherOfUser配置开始********************/

//    @Value("${task.voucher.count.voucher.ofUser}")
//    private String countVoucherOfUserJsonStr;
//
//    @Bean(name = "countVoucherOfUser")
//    public CountVoucherOfUser countVoucherOfUser() {
//        return new CountVoucherOfUser();
//    }
//
//    @Bean(initMethod = "init")
//    public JobScheduler countVoucherOfUserJobScheduler(@Qualifier("countVoucherOfUser")CountVoucherOfUser countVoucherOfUser,ObjectMapper objectMapper,@Qualifier("zkRegistryCenterProxy") ZkRegistryCenterProxy zkRegistryCenterProxy) throws Exception
//    {
//        logger.info("##########初始化Job:{}##########",countVoucherOfUser.getClass().getName());
//        return super.jobScheduler(countVoucherOfUser, countVoucherOfUserJsonStr, objectMapper, zkRegistryCenterProxy);
//    }

     /**************countVoucherOfUser配置结束********************/

    /***************perfectUserPoint配置开始********************/

    @Value("${task.user.perfect.point}")
    private String perfectUserPointJsonStr;

    @Bean(name = "perfectUserPoint")
    public PerfectUserPoint perfectUserPoint() {
        return new PerfectUserPoint();
    }

    @Bean(initMethod = "init")
    public JobScheduler perfectUserPointJobScheduler(@Qualifier("perfectUserPoint")PerfectUserPoint perfectUserPoint,ObjectMapper objectMapper,@Qualifier("zkRegistryCenterProxy") ZkRegistryCenterProxy zkRegistryCenterProxy) throws Exception
    {
        logger.info("##########初始化Job:{}##########",perfectUserPoint.getClass().getName());
        return super.jobScheduler(perfectUserPoint, perfectUserPointJsonStr, objectMapper, zkRegistryCenterProxy);
    }

    /**************orderWeightCount配置结束********************/



}
