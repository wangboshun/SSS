package com.zny.system.application.api;

import cn.dev33.satoken.stp.StpUtil;
import com.zny.common.enums.UserTypeEnum;
import com.zny.common.result.MessageCodeEnum;
import com.zny.common.result.SaResultEx;
import com.zny.common.utils.ReflectUtils;
import com.zny.system.model.api.ApiModel;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author WBS
 * Date:2022/9/15
 */

@Aspect
@Component
@Order(1)
public class ApiAspect {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ApiApplication apiApplication;

    public ApiAspect(ApiApplication apiApplication) {
        this.apiApplication = apiApplication;
    }

    /**
     * 控制器切面
     */
    @Pointcut("execution(* com.zny.*.controller..*.*(..))")
    public void resource() {

    }

    /**
     * 拦截控制器请求
     *
     * @param pjp 环绕通知
     */
    @Around("resource()")
    public Object around(ProceedingJoinPoint pjp) {
        Object result;
        try {
            Object[] args = pjp.getArgs();
//            if (StpUtil.isLogin()) {
//                Object userType = StpUtil.getSession().get("userType");
//                //不是超级管理员
//                if (!userType.equals(UserTypeEnum.SUPER.getIndex())) {
//                    String userId = (String) StpUtil.getSession().get("userId");
//                    //检测api
//                    if (!checkApi(userId, pjp)) {
//                        return SaResultEx.error(MessageCodeEnum.AUTH_INVALID);
//                    }
//                }
//            }
            result = pjp.proceed(args);
        }
        catch (Throwable e) {
            logger.error(e.getMessage());
            result = SaResultEx.error(MessageCodeEnum.EXCEPTION);
        }

        return result;
    }

    /**
     * 检查Api
     */
    private boolean checkApi(String userId, ProceedingJoinPoint pjp) {
        try {
            String api = ReflectUtils.getApiUrl(pjp);
            List<ApiModel> apis = apiApplication.getApiByUser(userId);
            return apis.stream().anyMatch(x -> x.getApi_code().equals(api));
        }
        catch (NoSuchMethodException e) {
            logger.error("检查api异常", e);
        }
        return false;
    }
}
