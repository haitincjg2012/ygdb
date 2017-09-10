package com.apec.framework.rockmq.vo;

import java.util.Date;
import java.util.Map;

import com.apec.framework.common.enums.Realm;
import com.apec.framework.common.enumtype.MessageTemplate;
import com.apec.framework.common.enumtype.MessageType;
import com.apec.framework.dto.BaseVO;

/**
 * Title: 消息体
 *
 * @author yirde  2017/7/3.
 */
public class MessageBodyVO extends BaseVO<Long> implements IMQBody{

    /**
	 * 标题
	 */
	private String title;

    /**
     * 自定义内容
     */
    private String content;
    
    /**
     * 替换模板内容key-value
     * */
    private Map<String, String> contentMap;
    
    /**
     * 模板key值
     * */
    private MessageTemplate templateKey;

    /**
     * 发送时间
     */
    private Date sentTime;

    /**
     * 所在的实体域
     */
    private Realm realm;

    /**
     * 消息类型
     */
    private MessageType type; 
    
    /**
     * 是否全部通知 Y 全部通知 ，N  指定用户ID 通知
     */
    private boolean allReceiver;
    
    public boolean isTemplateFlag() {
		return templateFlag;
	}

	public void setTemplateFlag(boolean templateFlag) {
		this.templateFlag = templateFlag;
	}

	/**
     * 是否使用动态模板
     * */
    public boolean templateFlag;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Map<String, String> getContentMap() {
		return contentMap;
	}

	public void setContentMap(Map<String, String> contentMap) {
		this.contentMap = contentMap;
	}

	public MessageTemplate getTemplateKey() {
		return templateKey;
	}

	public void setTemplateKey(MessageTemplate templateKey) {
		this.templateKey = templateKey;
	}

	public Date getSentTime() {
		return sentTime;
	}

	public void setSentTime(Date sentTime) {
		this.sentTime = sentTime;
	}

	public Realm getRealm() {
		return realm;
	}

	public void setRealm(Realm realm) {
		this.realm = realm;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public boolean isAllReceiver() {
		return allReceiver;
	}

	public void setAllReceiver(boolean allReceiver) {
		this.allReceiver = allReceiver;
	}

}
