package com.apec.framework.mail.engin;

import com.apec.framework.log.InjectLogger;
import com.apec.framework.mail.util.Constants;
import com.apec.framework.mail.vo.Mail;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by wubi on 2017/8/4.
 * @author xxx
 */
@Component
public class HtmlMailEngin extends AbstractMailEngin {

    /**
     * //执行者
     */
    @Autowired
    private JavaMailSender mailSender;

    @InjectLogger
    private Logger logger;

    @Override
    public void sendMail(Mail mail) throws MessagingException, FileNotFoundException {
        logger.info("using html template send mail");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(mail.getMailFrom());
        helper.setTo(mail.getMailTo());
        helper.setSubject(mail.getSubject());
        helper.setText(
                "<html><body><img src=\"cid:springcloud\" ></body></html>",
                true);
        // 发送图片
        File file = ResourceUtils.getFile("classpath:static"
                + Constants.SF_FILE_SEPARATOR + "image"
                + Constants.SF_FILE_SEPARATOR + "springcloud.png");
        helper.addInline("springcloud", file);

        // TODO 发送附件

    }
}
