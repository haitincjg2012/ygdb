package com.apec.framework.elasticsearch.vo;

/**
 * Title: ES 部分更新VO
 *
 * @author yirde  2017/7/11.
 */
public class EsUpdateDocRequestVO {

    /**
     * DOC
     */
    private Object doc;

    public Object getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public EsUpdateDocRequestVO(Object doc) {
        this.doc = doc;
    }

    public static EsUpdateDocRequestVO  getInstnce(Object doc){
        return new EsUpdateDocRequestVO(doc);
    }

    @Override
    public String toString(){
        return super.toString();
    }

}
