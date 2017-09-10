package com.apec.framework.common.tools;

import com.apec.framework.common.util.SpringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 类 编 号：
 * 类 名 称：ApplicationContextRegister
 * 内容摘要：基本描述：SpringUtil注入applicationContext
 * 创建日期：2016/10/17
 * 编码作者：
 */
@Component
public class ApplicationContextRegister implements ApplicationContextAware
{
    private static final Log log = LogFactory.getLog( ApplicationContextRegister.class );

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        log.debug( "ApplicationContextRegister.setApplicationContext:applicationContext" + applicationContext );
        SpringUtil.setApplicationContext( applicationContext );
    }
}
