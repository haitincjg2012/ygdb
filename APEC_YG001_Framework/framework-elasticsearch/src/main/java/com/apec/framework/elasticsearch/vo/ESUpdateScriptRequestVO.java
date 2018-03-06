package com.apec.framework.elasticsearch.vo;

/**
 * Title:ES Script
 *
 * @author yirde  2017/7/11.
 */
public class EsUpdateScriptRequestVO {

    /**
     * script
     */
    private String script;

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public EsUpdateScriptRequestVO(String script) {
        this.script = script;
    }

    public static EsUpdateScriptRequestVO  getInstnce(String script){
        return new EsUpdateScriptRequestVO(script);
    }

    @Override
    public String toString(){
       return super.toString();
    }

}
