package com.apec.framework.elasticsearch.vo;

/**
 * Title: ES 部分更新VO
 *
 * @author yirde  2017/7/11.
 */
public class ESUpdateDocRequestVO {

    //DOC
    private Object doc;

    public Object getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public ESUpdateDocRequestVO(Object doc) {
        this.doc = doc;
    }

    public static ESUpdateDocRequestVO  getInstnce(Object doc){
        return new ESUpdateDocRequestVO(doc);
    }
}
