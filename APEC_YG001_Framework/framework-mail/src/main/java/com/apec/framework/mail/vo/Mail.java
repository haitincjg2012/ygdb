package com.apec.framework.mail.vo;

import com.apec.framework.common.enumtype.MailType;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by wubi on 2017/8/4.
 * @author xxx
 */
public class Mail implements Serializable{

    /**
     * 必填参数 //主题
     */
    private String subject;

    /**
     * //收件人
     */
    private String[] mailTo;

    /**
     * //发件人
     */
    private String mailFrom;

    /**
     * //邮件内容
     */
    private String content;

    /**
     * //邮件发送类型(text,html,freemarker,thymeleaf)
     */
    private MailType type;

    /**
     * 选填参数//邮件模板
     */
    private String template;

    /**
     * //自定义参数
     */
    private HashMap<String,String> kvMap;

    public Mail(String subject, String content) {
        this.type = MailType.TEXT;
        this.subject = subject;
        this.content = content;
    }

    public Mail(String subject, String[] mailTo, String mailFrom, MailType type) {
        this.subject = subject;
        this.mailTo = mailTo;
        this.mailFrom = mailFrom;
        this.type = type;
    }

    public Mail(String subject, String[] mailTo, String mailFrom) {
        this.subject = subject;
        this.mailTo = mailTo;
        this.mailFrom = mailFrom;
    }

    public Mail(String subject, String[] mailTo, String mailFrom, String content, MailType type) {
        this.subject = subject;
        this.mailTo = mailTo;
        this.mailFrom = mailFrom;
        this.content = content;
        this.type = type;
    }

    public Mail(String subject, String[] mailTo, String mailFrom, String content, MailType type, String template) {
        this.subject = subject;
        this.mailTo = mailTo;
        this.mailFrom = mailFrom;
        this.content = content;
        this.type = type;
        this.template = template;
    }

    public Mail() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String[] getMailTo() {
        return mailTo;
    }

    public void setMailTo(String[] mailTo) {
        this.mailTo = mailTo;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MailType getType() {
        return type;
    }

    public void setType(MailType type) {
        this.type = type;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public HashMap<String, String> getKvMap() {
        return kvMap;
    }

    public void setKvMap(HashMap<String, String> kvMap) {
        this.kvMap = kvMap;
    }
}
