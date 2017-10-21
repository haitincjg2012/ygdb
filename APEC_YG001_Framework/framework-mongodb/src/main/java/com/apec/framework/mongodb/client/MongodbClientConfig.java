package com.apec.framework.mongodb.client;

import com.apec.framework.log.InjectLogger;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collections;

/**
 * Title:
 *
 * @author yirde  2017/10/19.
 */
@Configuration
@EnableMongoRepositories
public class MongodbClientConfig  extends AbstractMongoConfiguration{

    /**
     * Host Name
     */
    @Value("${mongodb.host}")
    private String host;

    /**
     * Port
     */
    @Value("${mongodb.host}")
    private int port;

    /**
     * Login by UserName
     */
    @Value("${mongodb.username}")
    private String userName;

    /**
     * Login by Password
     */
    @Value("${mongodb.password}")
    private String password;

    /**
     *  dataBase
     */
    @Value("${mongodb.database}")
    private String dataBase;


    @InjectLogger
    private Logger logger;

    /**
     * Get Single MongoCredential
     * @return MongoCredential
     */
    private MongoCredential getSingleMongoCredential(){
        return  MongoCredential.createCredential(userName, dataBase, password.toCharArray());
    }

    /**
     * MongoClientFactoryBean config
     */
    @Bean
    public MongoClientFactoryBean getMongoClientFactoryBean() {
        MongoClientFactoryBean mongoClientFactoryBean = new MongoClientFactoryBean();
        mongoClientFactoryBean.setHost(host);
        mongoClientFactoryBean.setPort(port);

        mongoClientFactoryBean.setCredentials(new MongoCredential[]{
            getSingleMongoCredential()
        });
        logger.info("=========================Initialize the configuration of  mongoClientFactoryBean =========================");
        return mongoClientFactoryBean;
    }

    /**
     *  Mongo Client config
     * @return MongoClient
     */
    @Override
    @Bean
    public MongoClient mongoClient(){
        MongoClient mongoClient = null;
        try {
            mongoClient = new MongoClient(Collections.singletonList(new ServerAddress(host, port)),
                                            Collections.singletonList(getSingleMongoCredential()));

            logger.info("=========================Initialize the configuration of  MongoClientClient =========================");
        }catch (Exception ex){
            logger.error("Start Mongo GetClient Failed! ",ex);
            logger.info("=========================Initialize the configuration of  MongoClientClient Failed =========================");
        }
        return mongoClient;
    }

    /**
     * MongoDbFactory config
     * @return MongoDbFactory
     */
    @Bean
    public MongoDbFactory getMongoDbFactory() {
        return new SimpleMongoDbFactory(mongoClient(), dataBase);
    }

    @Override
    protected String getDatabaseName() {
        return dataBase;
    }
}
