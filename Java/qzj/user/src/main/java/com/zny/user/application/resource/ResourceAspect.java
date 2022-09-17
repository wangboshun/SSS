package com.zny.user.application.resource;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.google.common.collect.Table;
import com.zny.common.utils.ReflectUtils;
import com.zny.user.model.user.UserModel;
import com.zny.user.model.user.UserTypeEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * Date:2022/9/15
 */

@Aspect
@Component
@Order(1)
public class ResourceAspect {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ResourceApplication resourceApplication;

    public ResourceAspect(ResourceApplication resourceApplication) {
        this.resourceApplication = resourceApplication;
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
            if (StpUtil.isLogin()) {
                UserModel user = (UserModel) StpUtil.getSession().get("userInfo");
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
            logger.error("检查api异常", e);
        }
        return false;
    }
}