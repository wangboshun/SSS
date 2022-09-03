package com.zny.common.aop;

import cn.dev33.satoken.spring.SpringMVCUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.zny.common.event.ApiLogEvent;
import com.zny.common.utils.DateUtils;
import com.zny.common.utils.IpUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WBS
 * 控制器拦截
 * Date:2022/9/1
 */

@Aspect
@Component
public class ControllerAspect {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Pointcut("execution(* com.zny.*.controller..*.*(..))")
    public void apiLog() {

    }

    @Around("apiLog()")
    public Object around(ProceedingJoinPoint pjp) {

        Object result = null;
        try {
            HttpServletRequest httpServletRequest = SpringMVCUtil.getRequest();

            //如果未登录
            if (!"/user/login".equals(httpServletRequest.getRequestURI()) && !StpUtil.isLogin()) {
                return SaResult.get(401, "请登录！", null);
            }

            LocalDateTime start = LocalDateTime.now();
            Object[] args = pjp.getArgs();
            result = pjp.proceed(args);
            SaResult sa = (SaResult) result;
            LocalDateTime end = LocalDateTime.now();
            Map<String, Object> map = new HashMap<>(10);
            map.put("user_id", StpUtil.getLoginId().toString());
            map.put("spend", Duration.between(start, end).toMillis() / 1000f);
            map.put("url", httpServletRequest.getRequestURI());
            map.put("method", httpServletRequest.getMethod());
            map.put("params", httpServletRequest.getQueryString());
            map.put("ip", IpUtils.getRemoteIp(httpServletRequest));
            map.put("code", sa.getCode());
            map.put("start_time", DateUtils.dateToStr(start));
            map.put("end_time", DateUtils.dateToStr(end));
            
            //GET请求不存储日志
            if (!"GET".equals(httpServletRequest.getMethod())) {
                map.put("data", sa.toString());
            }

            new Thread(() -> {
                applicationEventPublisher.publishEvent(new ApiLogEvent(map));
            }).start();
        } catch (Throwable e) {
            logger.error(e.getMessage());
            result = SaResult.error("内部异常！");
        }

        return result;
    }
}
