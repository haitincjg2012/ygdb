package com.apec.framework.elasticsearch.vo;

/**
 * Title: 取回一个文档的返回
 *
 * @author yirde  2017/7/11.
 */
public class ESGetSingleResponseVO<V> extends  ESResponseVO {

    /**
     * 是否发现  true 发现 ，false 未发现
     */
    private boolean found;

    /**
     * Source 对象
     */
    private V  source;


    public boolean getFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public V getSource() {
        return source;
    }

    public void setSource(V source) {
        this.source = source;
    }
}
