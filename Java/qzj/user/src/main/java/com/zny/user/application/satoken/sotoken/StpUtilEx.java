package com.zny.user.application.satoken.sotoken;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.strategy.SaStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author WBS
 * Date:2022/9/8
 */

@Component
public class StpUtilEx extends StpUtil {

    public static SaTokenImpl impl;
    @Autowired
    private SaTokenImpl saTokenImpl;

    public static List<String> getMenuList() {
        return getMenuList(getLoginId(), getLoginType());
    }

    public static List<String> getMenuList(Object loginId, String loginType) {
        return impl.getMenuList(loginId, loginType);
    }

    public static boolean hasMenu(String menu) {
        return hasMenu(getLoginId(), menu);
    }

    public static boolean hasMenu(Object loginId, String menu) {
        List<String> list = getMenuList(loginId, getLoginType());
        return SaStrategy.me.hasElement.apply(list, menu);
    }

    @PostConstruct
    public void init() {
        impl = saTokenImpl;
    }

}
