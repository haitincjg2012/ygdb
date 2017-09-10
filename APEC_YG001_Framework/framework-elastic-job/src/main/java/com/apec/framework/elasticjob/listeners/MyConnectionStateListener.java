package com.apec.framework.elasticjob.listeners;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyConnectionStateListener implements ConnectionStateListener 
{
    private static Logger logger = LoggerFactory.getLogger(MyConnectionStateListener.class);

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState)
    {
        logger.info("#######{} stateChanged: {}", client.getZookeeperClient().getCurrentConnectionString(), newState);
        switch(newState){
            case RECONNECTED :
                try{
                    logger.info("reconected sessionTimeout:"+client.getZookeeperClient().getZooKeeper().getSessionTimeout());
                } catch (Exception e) {
                    logger.error("recreate node({}) error:{}", e);
                }
                break;
            case LOST :
                
                break;
            case CONNECTED :
                
                break;
            case SUSPENDED :
                
                break;
            default:
                
                break;    
        }
        
    }

    
    
}
