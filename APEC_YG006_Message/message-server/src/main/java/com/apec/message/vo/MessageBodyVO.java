package com.apec.message.vo;

import java.util.Date;
import java.util.Map;

import com.apec.framework.common.enums.Realm;
import com.apec.framework.common.enumtype.MessageTemplate;
import com.apec.framework.common.enumtype.MessageType;
import com.apec.framework.dto.BaseVO;
import lombok.Data;

/**
 * Title: 消息体
 *
 * @author yirde  2017/7/3.
 */
@Data
public class MessageBodyVO extends BaseVO<Long> {

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
    private Date sendTime;

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
    private Boolean allReceiver;

	/**
     * 是否使用动态模板
     * */
    public Boolean templateFlag;



}
