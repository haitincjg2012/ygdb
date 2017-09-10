package com.apec.framework.mail.engin;

import com.apec.framework.mail.vo.Mail;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by wubi on 2017/8/4.
 */
public abstract class MailEngin {
    public abstract void sendMail(Mail mail) throws MessagingException, IOException, TemplateException;
}
