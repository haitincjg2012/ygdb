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

    // Put
    String OPREATION_PUT = "PUT";

    //Post update
    String OPREATION_POST_UPDATE = "/_update";

    //mapping
    String OPREATION_MAPPING = "/_mapping";

    //alias
    String OPREATION_ALIAS = "/_alias";

    String OPREATION_ALIASES = "/_aliases";

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
     * 索引路径  下架供求
     */
    String INDEX_URL_OFF_SELL = "/product/offsell/" ;

    /**
     * 仓库索引
     */
    String INDEX_URL_DEPORT_ORG = "/depot/orginfo/";

    /**
     * 代办索引
     */
    String INDEX_URL_DAIBAN_ORG = "/agency/orginfo/";

    /**
     * 客商索引
     */
    String INDEX_URL_KESHAN_ORG = "/merchant/orginfo/";
}
