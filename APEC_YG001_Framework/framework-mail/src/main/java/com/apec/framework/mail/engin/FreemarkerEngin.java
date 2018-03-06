package com.apec.framework.mail.engin;

import com.apec.framework.log.InjectLogger;
import com.apec.framework.mail.vo.Mail;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import freemarker.template.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wubi on 2017/8/4.
 * @author xxx
 */
@Component
public class FreemarkerEngin extends AbstractMailEngin {

    /**
     * /执行者
     */
    @Autowired
    private JavaMailSender mailSender;

    /**
     * //freemarker
     */
    @Autowired
    public Configuration configuration;

    @InjectLogger
    private Logger logger;

    @Override
    public void sendMail(Mail mail) throws MessagingException, IOException, TemplateException {
        logger.info("using Freemarker template send mail");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(mail.getMailFrom());
        helper.setTo(mail.getMailTo());
        helper.setSubject(mail.getSubject());
        Map<String, Object> model = new HashMap<>(16);
        model.put("content", mail.getContent());
        Template template = configuration.getTemplate(mail.getTemplate()+".flt");
        String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        helper.setText(text, true);
        mailSender.send(message);
    }
}
