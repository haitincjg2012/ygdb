package com.apec.framework.elasticjob.config;

import lombok.Data;

/**
 * @author xxx
 */
@Data
public class SimpleJobConfigItems {

	/**
	 * 任务名称
	 */
	private String jobName;

	/**
	 *  //表达式，用于配置作业触发时间
	 */
	private String cron;

	/**
	 * //作业分片总数
	 */
	private int shardingTotalCount;

	/**
	 * //分片序列号和参数用等号分隔，多个键值对用逗号分隔，分片序列号从0开始，不可大于或等于作业分片总数，如：	0=a,1=b,2=c
	 */
	private String shardingItemParameters;

	/**
	 * //监控作业运行时状态，每次作业执行时间和间隔时间均非常短的情况，建议不监控作业运行时状态以提升效率。因为是瞬时状态，所以无必要监控。请用户自行增加数据堆积监控。并且不能保证数据重复选取，应在作业中实现幂等性。每次作业执行时间和间隔时间均较长的情况，建议监控作业运行时状态，可保证数据不会重复选取。
	 */
	private boolean monitorExecution;

	/**
	 * //是否开启失效转移。仅monitorExecution开启，失效转移才有效
	 */
	private boolean failover;

	/**
	 * //作业描述信息
	 */
	private String description;

	/**
	 * //作业是否禁止启动。可用于部署作业时，先禁止启动，部署结束后统一启动
	 */
	private boolean disabled;

	/**
	 * //本地配置是否可覆盖注册中心配置。如果可覆盖，每次启动作业都以本地配置为准
	 */
	private boolean overwrite;

	/**
	 * //作业监控端口。建议配置作业监控端口, 方便开发者dump作业信息。使用方法: echo “dump” | nc 127.0.0.1 9888
	 */
	private int monitorPort;

}
