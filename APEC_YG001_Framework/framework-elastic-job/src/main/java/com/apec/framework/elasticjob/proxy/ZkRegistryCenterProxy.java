package com.apec.framework.elasticjob.proxy;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.apec.framework.elasticjob.listeners.MyConnectionStateListener;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

public class ZkRegistryCenterProxy  extends ZookeeperRegistryCenter implements InitializingBean, DisposableBean{
    
    private static Logger logger = LoggerFactory.getLogger(ZkRegistryCenterProxy.class);

	public ZkRegistryCenterProxy(ZookeeperConfiguration zkConfig) {
		super(zkConfig);
	}

	@Override
	public void destroy() throws Exception {
	}

	@Override
	@PostConstruct
	public void afterPropertiesSet() throws Exception {
	    logger.info("#####  ZookeeperRegistryCenter init  #######");
		super.init();
		//新增zk ConnectionState listener
        MyConnectionStateListener myConnectionStateListener = new MyConnectionStateListener();
        super.getClient().getConnectionStateListenable().addListener(myConnectionStateListener);
	}

}
