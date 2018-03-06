package com.apec.framework.mail.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wubi on 2017/8/4.
 * @author xxx
 */
@Configuration
public class MailConfig {
    @Value("${spring.mail.username}")
    private String mailFrom;

    @Value("${spring.mail.to}")
    private String mailTo;

    public String[] getMailTo() {
        return mailTo.split(",");
    }

    public String getMailFrom() {
        return mailFrom;
    }
}
