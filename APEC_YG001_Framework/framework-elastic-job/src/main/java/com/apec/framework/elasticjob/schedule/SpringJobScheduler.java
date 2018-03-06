package com.apec.framework.elasticjob.schedule;

import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.google.common.base.Optional;
import com.apec.framework.elasticjob.util.AopTargetUtils;

/**
 * @author xxx
 */
public class SpringJobScheduler  extends JobScheduler{
   
   private final ElasticJob elasticJob;
   
   public SpringJobScheduler(final ElasticJob elasticJob, final CoordinatorRegistryCenter regCenter, final LiteJobConfiguration jobConfig, final ElasticJobListener... elasticJobListeners) {
       super(regCenter, jobConfig, getTargetElasticJobListeners(elasticJobListeners));
       this.elasticJob = elasticJob;
   }
   
   public SpringJobScheduler(final ElasticJob elasticJob, final CoordinatorRegistryCenter regCenter, final LiteJobConfiguration jobConfig, 
                             final JobEventConfiguration jobEventConfig, final ElasticJobListener... elasticJobListeners) {
       super(regCenter, jobConfig, jobEventConfig, getTargetElasticJobListeners(elasticJobListeners));
       this.elasticJob = elasticJob;
   }
   
   private static ElasticJobListener[] getTargetElasticJobListeners(final ElasticJobListener[] elasticJobListeners) {
       final ElasticJobListener[] result = new ElasticJobListener[elasticJobListeners.length];
       for (int i = 0; i < elasticJobListeners.length; i++) {
           result[i] = (ElasticJobListener) AopTargetUtils.getTarget(elasticJobListeners[i]);
       }
       return result;
   }
   
   @Override
   protected Optional<ElasticJob> createElasticJobInstance() {
       return Optional.fromNullable(elasticJob);
   }
}
