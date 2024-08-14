package com.feilu.api.common.annotation;

import com.feilu.api.common.config.DynamicDatasourceHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 动态数据源切面
 * @author dzh
 */
@Slf4j
@Aspect
@Component
public class DynamicDatasourceAspect {

    /**
     * 切点，切的是带有@DDS的注解
     */
    @Pointcut("@annotation(com.feilu.api.common.annotation.DDS)")
    public void dynamicDatasourcePointcut(){

    }

    /**
     * 环绕通知
     */
    @Around("dynamicDatasourcePointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String datasourceKey = "master";

        // 获取类上的注解
        Class<?> targetClass = joinPoint.getTarget().getClass();
        DDS classAnnotation = targetClass.getAnnotation(DDS.class);

        // 获取方法上的注解
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        DDS methodAnnotation = methodSignature.getMethod().getAnnotation(DDS.class);

        if (Objects.nonNull(methodAnnotation)) {
            datasourceKey = methodAnnotation.value();
        } else if (Objects.nonNull(classAnnotation)) {
            datasourceKey = classAnnotation.value();
        }

        log.info("Switching to datasource: {}", datasourceKey);

        // 设置数据源
        DynamicDatasourceHolder.setDataSource(datasourceKey);
        try {
            return joinPoint.proceed();
        } finally {
            DynamicDatasourceHolder.removeDataSource();
        }
    }
}
