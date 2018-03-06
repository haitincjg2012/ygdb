package com.apec.framework.mail.engin;

import com.apec.framework.mail.vo.Mail;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by wubi on 2017/8/4.
 * @author xxx
 */
public abstract class AbstractMailEngin {

    /**
     * 发送mail
     * @param  mail mail
     * @throws MessagingException MessagingException
     * @throws IOException IOException
     * @throws TemplateException TemplateException
     */
    public abstract void sendMail(Mail mail) throws MessagingException, IOException, TemplateException;
}
