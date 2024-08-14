package com.feilu.api.common.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 动态数据源的注解
 * 用在类和方法上，方法上的优先级大于类上的
 * 默认值是 system
 * @author dzh
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface DDS {
    String value() default "system";
}
