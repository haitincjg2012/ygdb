package com.apec.framework.mail.engin.facotry;

import com.apec.framework.common.enumtype.MailType;
import com.apec.framework.common.util.BaseSpringUtil;
import com.apec.framework.mail.engin.*;
import org.springframework.stereotype.Component;

/**
 * Created by wubi on 2017/8/4.
 * @author xxx
 */
@Component
public class EnginFactory {
    public AbstractMailEngin createEngin(MailType type) {
        switch (type){
            case TEXT:
                return BaseSpringUtil.getApplicationContext().getBean(TextMailEngin.class);
            case HTML:
                return BaseSpringUtil.getApplicationContext().getBean(HtmlMailEngin.class);
            case FREEMARKER:
                return BaseSpringUtil.getApplicationContext().getBean(FreemarkerEngin.class);
            case THYMELEAF:
                return BaseSpringUtil.getApplicationContext().getBean(ThymeleafMailEngin.class);
            default:
                return BaseSpringUtil.getApplicationContext().getBean(TextMailEngin.class);
        }
    }
}
