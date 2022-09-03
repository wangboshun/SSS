package com.zny.common.config.satoken;

import cn.dev33.satoken.exception.DisableLoginException;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author WBS
 * Date:2022/9/3
 */

@ControllerAdvice
public class MySaTokenException {

    @ResponseBody
    @ExceptionHandler
    public SaResult handlerException(Exception e, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 如果是未登录异常
        if (e instanceof NotLoginException) {
            return SaResult.get(401, "请登录！", null);
        }
        // 如果是角色异常
        else if (e instanceof NotRoleException) {
            NotRoleException ee = (NotRoleException) e;
            System.out.println("所需的角色：" + ee.getRole());
            return SaResult.get(500, "角色权限不足！", null);
        }
        // 如果是权限异常
        else if (e instanceof NotPermissionException) {
            NotPermissionException ee = (NotPermissionException) e;
            System.out.println("所需的权限：" + ee.getPermission());
            return SaResult.get(500, "用户权限不足！", null);
        }
        // 如果是被封禁异常
        else if (e instanceof DisableLoginException) {
            DisableLoginException ee = (DisableLoginException) e;
            return SaResult.get(500, "账号被封禁：" + ee.getDisableTime() + "秒后解封", null);
        }
        // 其他异常, 输出：500 + 异常信息
        else {
            System.out.println("MySaTokenException："+e.getMessage());
            return SaResult.get(500, "内部异常：", null);
        }
    }
}
