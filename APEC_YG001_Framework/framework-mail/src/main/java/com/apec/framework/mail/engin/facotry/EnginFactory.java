package com.apec.framework.mail.engin.facotry;

import com.apec.framework.common.enumtype.MailType;
import com.apec.framework.common.util.SpringUtil;
import com.apec.framework.mail.engin.*;
import org.springframework.stereotype.Component;

/**
 * Created by wubi on 2017/8/4.
 */
@Component
public class EnginFactory {
    public MailEngin createEngin(MailType type) {
        switch (type){
            case TEXT:
                return SpringUtil.getApplicationContext().getBean(TextMailEngin.class);
            case HTML:
                return SpringUtil.getApplicationContext().getBean(HtmlMailEngin.class);
            case FREEMARKER:
                return SpringUtil.getApplicationContext().getBean(FreemarkerEngin.class);
            case THYMELEAF:
                return SpringUtil.getApplicationContext().getBean(ThymeleafMailEngin.class);
            default:
                return SpringUtil.getApplicationContext().getBean(TextMailEngin.class);
        }
    }
}
