package com.apec.framework.elasticsearch.vo;

/**
 * Title:ES Script
 *
 * @author yirde  2017/7/11.
 */
public class ESUpdateScriptRequestVO {

    //script
    private String script;

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public ESUpdateScriptRequestVO(String script) {
        this.script = script;
    }

    public static ESUpdateScriptRequestVO  getInstnce(String script){
        return new ESUpdateScriptRequestVO(script);
    }
}
