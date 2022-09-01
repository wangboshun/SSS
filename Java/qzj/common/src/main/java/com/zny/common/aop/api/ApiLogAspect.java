package com.zny.common.aop.api;

import com.zny.common.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author WBS
 * Date:2022/9/1
 */

@Slf4j
@Aspect
@Component
public class ApiLogAspect {

    @Pointcut("@annotation(com.zny.common.aop.api.ApiLog)")
    public void apiLog() {

    }

    @Around("apiLog()")
    public Object around(ProceedingJoinPoint pjp) {
        Object result = null;
        try {
            Object[] args = pjp.getArgs();
            result = pjp.proceed(args);

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest httpServletRequest = attributes.getRequest();

            String url = httpServletRequest.getRequestURI();
            String method = httpServletRequest.getMethod();
            String parameters = httpServletRequest.getQueryString();
            String ip = IpUtils.getRemoteIp(httpServletRequest);


            System.out.println("返回：" + result);
        } catch (Throwable e) {
            log.error(e.getMessage());
        }

        return result;
    }
}
