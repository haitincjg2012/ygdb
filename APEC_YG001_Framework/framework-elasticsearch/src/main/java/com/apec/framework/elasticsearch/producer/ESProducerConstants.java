package com.apec.framework.elasticsearch.producer;

/**
 * Title: Es Producer Constants
 *
 * @author yirde  2017/7/10.
 */
public interface ESProducerConstants {

    //Client 操作类型
    String OPREATION_GET = "GET";

    // Post
    String OPREATION_POST = "POST";

    //Post update
    String OPREATION_POST_UPDATE = "/_update";

    //Delete
    String OPREATION_DELETE = "DELETE";

    //HEAD 检索文档是否存在
    String OPREATION_HEAD = "HEAD";

    //Null
    String JSON_NULL = "{}";

    // 响应的状态码
    int  RESPONSE_CODE = 200;

    //Buffer Size 1KB
    int  BUFFER_SIZE = 1024;

    /**
     * 索引路径
     *  产品中心
     */
    String INDEX_URL_PRODUCT = "/apec/myblog/";

    /**
     * 索引路径  热门推荐
     */
    String INDEX_URL_RECOMMEND = "/blog/recommend/" ;

    /**
     * 索引路径  下架供求
     */
    String INDEX_URL_OFF_SELL = "/product/offsell/" ;

}