package com.zny.common.aop;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.zny.common.utils.DateUtils;
import com.zny.common.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author WBS
 * Date:2022/9/1
 */

@Slf4j
@Aspect
@Component
public class ApiLogAspect {

    @Pointcut("execution(* com.zny.*.controller..*.*(..))")
    public void apiLog() {

    }

    @Around("apiLog()")
    public Object around(ProceedingJoinPoint pjp) {
        Object result = null;
        try {
            LocalDateTime start = LocalDateTime.now();
            Object[] args = pjp.getArgs();
            result = pjp.proceed(args);
            LocalDateTime end = LocalDateTime.now();

            if (result instanceof SaResult) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest httpServletRequest = attributes.getRequest();
                SaResult sa = (SaResult) result;

                String loginId = StpUtil.getLoginId().toString();
                Float tm = Duration.between(start, end).toMillis() / 1000f;
                String url = httpServletRequest.getRequestURI();
                String method = httpServletRequest.getMethod();
                String params = httpServletRequest.getQueryString();
                String ip = IpUtils.getRemoteIp(httpServletRequest);
                Integer code = sa.getCode();
                String data = sa.toString();
                String startTime = DateUtils.dateToStr(start);
                String endTime = DateUtils.dateToStr(end);
            }
        } catch (Throwable e) {
            log.error(e.getMessage());
        }

        return result;
    }
}
