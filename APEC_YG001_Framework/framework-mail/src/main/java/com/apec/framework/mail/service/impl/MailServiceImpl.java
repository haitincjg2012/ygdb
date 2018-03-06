package com.apec.framework.mail.service.impl;

import com.apec.framework.common.exception.ServerException;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.mail.config.MailConfig;
import com.apec.framework.mail.engin.AbstractMailEngin;
import com.apec.framework.mail.engin.facotry.EnginFactory;
import com.apec.framework.mail.service.MailService;
import com.apec.framework.mail.vo.Mail;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by wubi on 2017/8/4.
 * @author xxx
 */
@Service
public class MailServiceImpl implements MailService{

    @Autowired
    private EnginFactory facotry;

    @Autowired
    private MailConfig config;

    @Value("${spring.profiles.active}")
    private String profile;

    @InjectLogger
    private Logger logger;

    /**
     * 发送邮件服务 根据type选择不同模板发送
     * @param mail mail
     * @param genEnv 标题是否生成环境信息
     * @throws Exception Exception
     */
    @Override
    @Async
    public void sendMail(Mail mail, boolean genEnv){
        validate(mail);
        handlerEnv(mail, genEnv);
        send(mail);
    }

    @Override
    public void sendMail(Mail mail) {
        validate(mail);
        send(mail);
    }

    private void send(Mail mail) {
        try {
            logger.info("=================get mail engin===================");
            AbstractMailEngin engin = facotry.createEngin(mail.getType());
            logger.info("============send mail start=================");
            engin.sendMail(mail);
            logger.info("============send mail end=================");
        }catch (Exception e) {
            logger.error("send mail error   mailSubject:{} # sendFrom:{} # sendTo:{}", mail.getSubject(), mail.getMailFrom(), mail.getMailTo());
            throw new ServerException("mail send failed", e);
        }
    }

    private void handlerEnv(Mail mail, boolean genEven) {
        if(genEven) {
            String subject = "environment [%s] %s";
            subject = String.format(subject, profile, mail.getSubject());
            mail.setSubject(subject);
        }
    }

    private void validate(Mail mail) {
        //判断参数是否满足要求 如果为空则使用默认值
        if (StringUtils.isBlank(mail.getMailFrom())) {
            mail.setMailFrom(config.getMailFrom());
        }
        if (null == mail.getMailTo()) {
            mail.setMailTo(config.getMailTo());
        }
    }
}
