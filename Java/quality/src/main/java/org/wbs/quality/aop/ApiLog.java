package org.wbs.quality.aop;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WBS
 * Api日志
 * Date:2022/6/2
 */

@Slf4j
public class ApiLog implements Runnable {

    private final ProceedingJoinPoint pjd;

    private final HttpServletRequest request;

    private final Long spend;

    private final Object result;

    private final Logger logger= LoggerFactory.getLogger("ApiLog");

    public ApiLog(ProceedingJoinPoint pjd, HttpServletRequest request, Object result, Long spend) {
        this.pjd = pjd;
        this.request = request;
        this.result = result;
        this.spend = spend;
    }

    @Override
    public void run() {

        Object[] args = this.pjd.getArgs();
        String[] argNames = ((MethodSignature) this.pjd.getSignature()).getParameterNames();
        Map<String, Object> params = new HashMap<>(args.length);
        for (int i = 0; i < argNames.length; i++) {
            params.put(argNames[i], args[i]);
        }

        Map<String, Object> json = new HashMap<>();
        json.put("params", params);
        json.put("name", this.pjd.getSignature().getName());
        json.put("time", this.spend + " ms");
        json.put("result", this.result);
        json.put("client", this.request.getRemoteHost() + ":" + this.request.getRemotePort());
        json.put("url", this.request.getRequestURL());
        json.put("method", this.request.getMethod());
        logger.info(new Gson().toJson(json));
    }
}
