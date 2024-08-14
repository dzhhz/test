package com.feilu.api.common.annotation;

import com.feilu.api.common.entity.ApiResponse;
import com.feilu.api.common.entity.Ret;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author dzh
 */
@Aspect
@Component
public class ValidationAspect {

    @Pointcut("@annotation(com.feilu.api.common.annotation.ValidateRequestData)")
    public void validateRequestDataPointcut() {}

    @Around("validateRequestDataPointcut()")
    public Object validateRequestData(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof Map<?, ?> requestParam) {
            // 类型检查和转换
            Object requestDataObj = requestParam.get("requestData");
            if (requestDataObj == null || StringUtils.isBlank(requestDataObj.toString())) {
                return Ret.fail("msg", "请求参数为空!");
            }
        } else {
            return Ret.fail("msg", "请求参数类型不正确!");
        }
        return joinPoint.proceed();
    }


    @Pointcut("@annotation(com.feilu.api.common.annotation.ValidateEsItemReq)")
    public void validateEsItemReqPointcut() {}

    @Around("validateEsItemReqPointcut()")
    public Object validateEsItemReq(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof Map<?, ?> requestParam) {
            // 类型检查和转换
            Object requestDataObj = requestParam.get("esItemReq").toString();
            if(requestDataObj == null || StringUtils.isBlank(requestDataObj.toString())){
                ApiResponse<Object> apiResponse = new ApiResponse<>();
                apiResponse.setState("fail");
                apiResponse.setMessage("请求参数为空!");
                return apiResponse;
            }
        } else {
            ApiResponse<Object> apiResponse = new ApiResponse<>();
            apiResponse.setState("fail");
            apiResponse.setMessage("请求参数类型不正确!");
            return apiResponse;
        }
        return joinPoint.proceed();
    }


}

