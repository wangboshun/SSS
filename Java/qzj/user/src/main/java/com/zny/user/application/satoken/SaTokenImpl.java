package com.zny.user.application.satoken;

import cn.dev33.satoken.stp.StpInterface;
import com.google.common.collect.Table;
import com.zny.user.application.ResourceApplication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author WBS
 * Date:2022/9/8
 */

@Service
public class SaTokenImpl implements StpInterface {

    private final ResourceApplication resourceApplication;

    public SaTokenImpl(ResourceApplication resourceApplication) {
        this.resourceApplication = resourceApplication;
    }

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        Table<String, String, String> table = resourceApplication.getPermissionByUser(loginId.toString());
        Set<String> strings = table.columnKeySet();
        return new ArrayList<>(strings);
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Table<String, String, String> table = resourceApplication.getRoleByUser(loginId.toString());
        Set<String> strings = table.columnKeySet();
        return new ArrayList<>(strings);
    }

    public List<String> getMenuList(Object loginId, String loginType) {
        Table<String, String, String> table = resourceApplication.getMenuByUser(loginId.toString());
        Set<String> strings = table.columnKeySet();
        return new ArrayList<>(strings);
    }
}
