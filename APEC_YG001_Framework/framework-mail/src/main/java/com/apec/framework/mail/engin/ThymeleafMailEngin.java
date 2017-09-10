package com.apec.framework.mail.engin;

import com.apec.framework.log.InjectLogger;
import com.apec.framework.mail.vo.Mail;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by wubi on 2017/8/4.
 */
@Component
public class ThymeleafMailEngin extends MailEngin{

    @Autowired
    private JavaMailSender mailSender;//执行者

    @Autowired
    private SpringTemplateEngine templateEngine;//thymeleaf

    @InjectLogger
    private Logger logger;

    @Override
    public void sendMail(Mail mail) throws MessagingException {
        logger.info("using thymeleaf template send mail");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(mail.getMailFrom());
        helper.setTo(mail.getMailTo());
        helper.setSubject(mail.getSubject());
        Context context = new Context();
        context.setVariable("email", mail);
        String text = templateEngine.process(mail.getTemplate(), context);
        helper.setText(text, true);
        mailSender.send(message);
    }
}
