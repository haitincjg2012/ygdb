package com.apec.framework.elasticsearch.vo;

/**
 * Title: POST 提交返回
 *
 * @author yirde  2017/7/10.
 */
public class ESPostResponseVO extends  ESResponseVO{

    /**
     * 结果
     */
    private String result;

    /**
     * 是否创建
     */
    private boolean created;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }
}
