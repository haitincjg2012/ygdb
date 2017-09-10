package com.apec.framework.elasticsearch.vo;

/**
 * Title: 通用响应内容
 *
 * @author yirde  2017/7/10.
 */
public class ESResponseVO {

    /**
     * 索引
     */
    private String index;

    /**
     * 类型
     */
    private String type ;

    /**
     * ID
     */
    private String id;

    /**
     * 版本
     */
    private int version;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
