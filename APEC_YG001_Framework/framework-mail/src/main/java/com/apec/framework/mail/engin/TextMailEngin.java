package com.apec.framework.mail.engin;

import com.apec.framework.log.InjectLogger;
import com.apec.framework.mail.vo.Mail;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Created by wubi on 2017/8/4.
 * @author xxx
 */
@Component
public class TextMailEngin extends AbstractMailEngin {

    /**
     * //执行者
     */
    @Autowired
    private JavaMailSender mailSender;

    @InjectLogger
    private Logger logger;

    @Override
    public void sendMail(Mail mail) {
        logger.info("using text template send mail");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mail.getMailFrom());
        message.setTo(mail.getMailTo());
        message.setSubject(mail.getSubject());
        message.setText(mail.getContent());
        mailSender.send(message);
    }
}
