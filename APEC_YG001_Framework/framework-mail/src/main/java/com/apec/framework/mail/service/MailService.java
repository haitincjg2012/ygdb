package com.apec.framework.mail.service;

import com.apec.framework.mail.vo.Mail;

/**
 * Created by wubi on 2017/8/4.
 */
public interface MailService {

    /**
     * 发送邮件
     * @param mail
     */
    void sendMail(Mail mail);

}
