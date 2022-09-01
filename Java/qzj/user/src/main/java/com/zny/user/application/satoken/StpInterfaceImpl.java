package com.zny.user.application.satoken;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WBS
 * Date:2022/9/1
 */
@Configuration
public class StpInterfaceImpl implements StpInterface {
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<String> list = new ArrayList<String>();
        list.add("user-add");
        list.add("user-delete");
        list.add("user-update");
        list.add("user-get");
        return list;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> list = new ArrayList<String>();
        list.add("admin");
        list.add("super-admin");
        return list;
    }
}
