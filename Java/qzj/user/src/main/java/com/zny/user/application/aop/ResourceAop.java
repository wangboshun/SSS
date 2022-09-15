package com.zny.user.application.aop;

import cn.dev33.satoken.spring.SpringMVCUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.google.common.collect.Table;
import com.zny.common.utils.ReflectUtils;
import com.zny.user.application.ResourceApplication;
import com.zny.user.model.user.UserModel;
import com.zny.user.model.user.UserTypeEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author WBS
 * Date:2022/9/15
 */

@Aspect
@Component
@Order(1)
public class ResourceAop {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ResourceApplication resourceApplication;

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
            HttpServletRequest httpServletRequest = SpringMVCUtil.getRequest();
            if (!"/user/login".equals(httpServletRequest.getRequestURI())) {
                UserModel user = (UserModel) StpUtil.getSession().get("user");

                //不是超级管理员
                if (!user.getUser_type().equals(UserTypeEnum.SUPER.getIndex())) {
                    //检测api
                    if (!checkApi(user.getId(), pjp)) {
                        return SaResult.get(500, "没有权限！", null);
                    }
                }
            }
            result = pjp.proceed(args);
        }
        catch (Throwable e) {
            logger.error(e.getMessage());
            result = SaResult.error("内部异常！");
        }

        return result;
    }

    /**
     * 检查Api
     */
    private boolean checkApi(String userId, ProceedingJoinPoint pjp) {
        try {
            String api = ReflectUtils.getApiUrl(pjp);
            Table<String, String, String> table = resourceApplication.getApiByUser(userId);
            return table.containsValue(api);
        }
        catch (NoSuchMethodException e) {

        }
        return false;
    }
}
