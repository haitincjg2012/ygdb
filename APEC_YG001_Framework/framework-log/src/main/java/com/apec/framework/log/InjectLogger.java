package com.apec.framework.log;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Title: 日志注解
 * @author yirde  2017/3/22
 */

@Retention(RUNTIME)
@Target(FIELD)
@Documented
public @interface InjectLogger {

}