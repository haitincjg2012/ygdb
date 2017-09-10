package com.apec.framework.elasticjob.config;

import lombok.Data;

@Data
public class EJZKconfigItem {
	
	private String serverLists;   //连接Zookeeper服务器的列表，包括IP地址和端口号，多个地址用逗号分隔，如: host1:2181,host2:2181
	private String namespace ="elastic-job";  //Zookeeper的命名空间
	private int baseSleepTimeMilliseconds = 1000; //等待重试的间隔时间的初始值，单位：毫秒
	private int maxSleepTimeMilliseconds = 3000; //等待重试的间隔时间的最大值，单位：毫秒
	private int maxRetries = 3;  //最大重试次数
	
    /**
     * 会话超时时间.
     * 单位毫秒.
     */
    private int sessionTimeoutMilliseconds = 60000;
    
    /**
     * 连接超时时间.
     * 单位毫秒.
     */
    private int connectionTimeoutMilliseconds = 15000;

}
