package com.apec.framework.elasticsearch.config;

import com.apec.framework.log.InjectLogger;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Title: APEC EC Client
 *
 *    Apache Http Async客户端默认启动一个调度程序线程,连接管理器使用的工作线程数量多达本地
 *    检测到的处理器数量（取决于 Runtime.getRuntime().availableProcessors()返回的数量）。
 *
 * @author yirde  2017/7/10.
 */
@Configuration
public class ApecEsProducerConfig {

    @Value("${elasticsearch.host1.name}")
    private String  hostOneName;

    @Value("${elasticsearch.host1.port}")
    private int hostOnePort;

    @Value("${elasticsearch.host2.name}")
    private String  hostTwoName;

    @Value("${elasticsearch.host2.port}")
    private int hostTwoPort;

    private static int ioThreadCount;

    private static int maxConnTotal;

    private static int maxConnPerRoute;

    private static int connectionTime;

    private static int socketTimeout;

    @InjectLogger
    private Logger logger;

    @Bean
    public RestClient getApecESProducer() {
        RestClient restClient = null;
        logger.info("=========================Initialize the Elasticsearch Client configuration。=========================");
        try {
            restClient = RestClient.builder(
                    new HttpHost(hostOneName, hostOnePort),
                    new HttpHost(hostTwoName, hostTwoPort))
                    .setRequestConfigCallback(RequestConfigCallback.getInstance())
                    .setHttpClientConfigCallback( HttpClientConfigCallback.getInstance())
                    .setMaxRetryTimeoutMillis(60_000).build();

            logger.info("=========================Initialize Elasticsearch Client Success。=========================");
            return restClient;
        }catch (Exception ex){
            logger.error("Initialize Elasticsearch Client Failed! ",ex);
            logger.info("=========================Initialize Elasticsearch Client  Failed。=========================");
        }
        return restClient;
    }

    /**
     * Time out Config
     */
    private static  class RequestConfigCallback implements  RestClientBuilder.RequestConfigCallback{

        @Override
        public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
            return requestConfigBuilder.setConnectTimeout(connectionTime)
                    .setSocketTimeout(socketTimeout);
        }
        private static RequestConfigCallback getInstance(){
            return  new RequestConfigCallback();
        }
    }

    /**
     * Io Thread Count
     */
    private static class HttpClientConfigCallback implements RestClientBuilder.HttpClientConfigCallback{

        @Override
        public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
            return httpClientBuilder.setMaxConnTotal(maxConnTotal)
                    .setMaxConnPerRoute(maxConnPerRoute)
                    .setDefaultIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(ioThreadCount).build());
        }
        private static HttpClientConfigCallback getInstance(){
            return  new HttpClientConfigCallback();
        }
    }

    @Value("${elasticsearch.ioThreadCount}")
    public void setIoThreadCount(int ioThreadCount) {
        ApecEsProducerConfig.ioThreadCount = ioThreadCount;
    }

    @Value("${elasticsearch.maxConnTotal}")
    public void setMaxConnTotal(int maxConnTotal) {
        ApecEsProducerConfig.maxConnTotal = maxConnTotal;
    }

    @Value("${elasticsearch.maxConnPerRoute}")
    public void setMaxConnPerRoute(int maxConnPerRoute) {
        ApecEsProducerConfig.maxConnPerRoute = maxConnPerRoute;
    }

    @Value("${elasticsearch.connectionTime}")
    public void setConnectionTime(int connectionTime) {
        ApecEsProducerConfig.connectionTime = connectionTime;
    }

    @Value("${elasticsearch.socketTimeout}")
    public void setSocketTimeout(int socketTimeout) {
        ApecEsProducerConfig.socketTimeout = socketTimeout;
    }
}
