package com.apec.framework.mail.service.impl;

import com.apec.framework.common.Constants;
import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.excel.ExcelStyle;
import com.apec.framework.common.exception.BusinessException;
import com.apec.framework.common.exception.ServerException;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.mail.config.MailConfig;
import com.apec.framework.mail.engin.MailEngin;
import com.apec.framework.mail.engin.facotry.EnginFactory;
import com.apec.framework.mail.service.MailService;
import com.apec.framework.mail.vo.Mail;
import com.netflix.loadbalancer.Server;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.constant.ErrorConstant;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Created by wubi on 2017/8/4.
 */
@Service
public class MailServiceImpl implements MailService{

    @Autowired
    private EnginFactory facotry;

    @Autowired
    private MailConfig config;

    @InjectLogger
    private Logger logger;

    /**
     * 发送邮件服务 根据type选择不同模板发送
     * @param mail
     * @throws Exception
     */
    @Override
    @Async
    public void sendMail(Mail mail){
        try {
            //判断参数是否满足要求 如果为空则使用默认值
            if (StringUtils.isBlank(mail.getMailFrom())) {
                mail.setMailFrom(config.getMailFrom());
            }
            if (null == mail.getMailTo()) {
                mail.setMailTo(config.getMailTo());
            }
            logger.info("=================get mail engin===================");
            MailEngin engin = facotry.createEngin(mail.getType());
            logger.info("============send mail start=================");
            engin.sendMail(mail);
            logger.info("============send mail end=================");
        }catch (Exception e) {
            logger.error("send mail error   mailSubject:{} # sendFrom:{} # sendTo:{}", mail.getSubject(), mail.getMailFrom(), mail.getMailTo());
            throw new ServerException("mail send failed", e);
        }
    }
}
