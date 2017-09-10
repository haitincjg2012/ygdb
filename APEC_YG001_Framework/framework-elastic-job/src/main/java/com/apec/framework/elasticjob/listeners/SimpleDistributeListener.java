package com.apec.framework.elasticjob.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.AbstractDistributeOnceElasticJobListener;

/**
 * AbstractDistributeOnceElasticJobListener --> 在分布式作业中只执行一次的监听器.
 */
public class SimpleDistributeListener extends AbstractDistributeOnceElasticJobListener
{
    private static Logger logger = LoggerFactory.getLogger(SimpleDistributeListener.class);

    private final long startedTimeoutMilliseconds;

    private final long completedTimeoutMilliseconds;

    public SimpleDistributeListener(final long startedTimeoutMilliseconds, final long completedTimeoutMilliseconds)
    {
        super(startedTimeoutMilliseconds, completedTimeoutMilliseconds);
        this.startedTimeoutMilliseconds = startedTimeoutMilliseconds;
        this.completedTimeoutMilliseconds = completedTimeoutMilliseconds;
    }

    // 最后一个定时任务执行前
    @Override
    public void doBeforeJobExecutedAtLastStarted(final ShardingContexts shardingContexts)
    {
        logger.info("doBeforeJobExecutedAtLastStarted:" + shardingContexts);
    }

    // 最后一个定时任务执行后
    @Override
    public void doAfterJobExecutedAtLastCompleted(final ShardingContexts shardingContexts)
    {
        logger.info("doAfterJobExecutedAtLastCompleted:" + startedTimeoutMilliseconds + ","
                    + completedTimeoutMilliseconds);
    }
}