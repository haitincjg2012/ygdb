package com.apec.framework.elasticjob.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;

/**
 * ElasticJobListener -----> 弹性化分布式作业监听器接口
 * @author xxx
 */
public class SimpleListener implements ElasticJobListener{
    
    private static Logger logger = LoggerFactory.getLogger(SimpleListener.class);
	
	@Override
	public void beforeJobExecuted(final ShardingContexts shardingContexts) {
	    logger.info("beforeJobExecuted={}, ShardingTotalCount={}",shardingContexts.getJobName() ,shardingContexts.getShardingTotalCount());
	}

	@Override
	public void afterJobExecuted(final ShardingContexts shardingContexts) {
	    logger.info("afterJobExecuted={}", shardingContexts.getJobName());
	}

}
