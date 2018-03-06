package com.apec.framework.mongodb.client;

import com.apec.framework.log.InjectLogger;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Arrays;
/**
 * Title:
 *
 * @author yirde  2017/10/19.
 */
@Configuration
@EnableMongoRepositories
@ComponentScan
public class MongodbClientConfig extends AbstractMongoConfiguration {

    /**
     * Host Name
     */
    @Value("${mongodb.host}")
    private String host;

    /**
     * Port
     */
    @Value("${mongodb.port}")
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


    @Override
    public String getDatabaseName() {
        return dataBase;
    }

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        return new MongoClient(serverAddress(), Arrays.asList(mongoCredential()));
    }

    @Bean
    public ServerAddress serverAddress() throws Exception {
        return new ServerAddress(host,port);
    }

    @Bean
    public MongoCredential mongoCredential() {
        return MongoCredential.createCredential(
                this.userName,
                getDatabaseName(),
                this.password.toCharArray());
    }

    @Bean
    @Override
    public MappingMongoConverter mappingMongoConverter() throws Exception {
        MappingMongoConverter converter = super.mappingMongoConverter();
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return converter;
    }

}
