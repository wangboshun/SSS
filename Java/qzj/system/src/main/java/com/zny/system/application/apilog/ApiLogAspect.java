package com.zny.system.application.apilog;

import cn.dev33.satoken.spring.SpringMVCUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.zny.common.eventbus.EventEnum;
import com.zny.common.eventbus.TopicAsyncEventBus;
import com.zny.common.utils.DateUtils;
import com.zny.common.utils.IpUtils;
import com.zny.common.utils.ReflectUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author WBS
 * 控制器拦截
 * Date:2022/9/1
 */

@Aspect
@Component
@Order(2)
public class ApiLogAspect {

    private final LinkedBlockingQueue<Map<String, Object>> logQueue = new LinkedBlockingQueue<>();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final TopicAsyncEventBus topicEventBus;

    public ApiLogAspect(TopicAsyncEventBus topicEventBus) {
        this.topicEventBus = topicEventBus;
    }

    /**
     * 控制器切面
     */
    @Pointcut("execution(* com.zny.*.controller..*.*(..))")
    public void apiLog() {

    }

    /**
     * 拦截控制器请求
     *
     * @param pjp 环绕通知
     */
    @Around("apiLog()")
    public Object around(ProceedingJoinPoint pjp) {
        Object result;
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
            if (sa.getCode() == 200 && !"GET".equals(httpServletRequest.getMethod())) {
                addApiLog(httpServletRequest, sa, ReflectUtils.getMethodDoc(pjp), start);
            }
        }
        catch (Throwable e) {
            logger.error(e.getMessage());
            result = SaResult.error("内部异常！");
        }

        return result;
    }

    /**
     * 添加接口日志
     *
     * @param httpServletRequest http请求
     * @param sa                 执行接口
     * @param start              开始时间
     */
    private void addApiLog(HttpServletRequest httpServletRequest, SaResult sa, String apiName, LocalDateTime start) {
        LocalDateTime end = LocalDateTime.now();
        Map<String, Object> map = new HashMap<>(11);
        map.put("user_id", StpUtil.getLoginId().toString());
        map.put("spend", Duration.between(start, end).toMillis() / 1000f);
        map.put("url", httpServletRequest.getRequestURI());
        map.put("method", httpServletRequest.getMethod());
        map.put("params", httpServletRequest.getQueryString());
        map.put("ip", IpUtils.getRemoteIp(httpServletRequest));
        map.put("code", sa.getCode());
        map.put("start_time", DateUtils.dateToStr(start));
        map.put("end_time", DateUtils.dateToStr(end));
        map.put("api_name", apiName);
        map.put("data", sa.toString());
        logQueue.offer(map);
    }

    /**
     * 接口日志定时任务
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    private void apiLogSchedule() {
        System.out.println(Thread.currentThread().getName() + "插入日志任务：" + DateUtils.dateToStr(LocalDateTime.now()));
        int size = logQueue.size();
        if (size < 1) {
            return;
        }

        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(logQueue.poll());
        }

        topicEventBus.post(EventEnum.APILOG.toString(), list);
    }
}
