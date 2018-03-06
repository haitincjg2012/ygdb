package com.apec.framework.elasticsearch.vo;

/**
 * Title: 取回一个文档的返回
 *
 * @author yirde  2017/7/11.
 */
public class EsGetSingleResponseVO<V> extends  EsResponseVO {

    /**
     * 是否发现  true 发现 ，false 未发现
     */
    private Boolean found;

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

    @Override
    public String toString(){
        return super.toString();
    }

}
