package com.example.springboot_1.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author WBS
 * Date:2022/8/29
 */

public class MyRealm extends AuthorizingRealm {

    Map<String, String> userMap = new HashMap<>(16);

    {
        userMap.put("admin", "123456");
        super.setName("myRealm");
    }

    public Set<String> getPermissionsByUserName(String userName) {
        Set<String> permissions = new HashSet<>();
        permissions.add("user:delete");
        permissions.add("user:add");
        return permissions;
    }

    public Set<String> getRolesByUserName(String userName) {
        Set<String> roles = new HashSet<>();
        roles.add("admin");
        roles.add("user");
        return roles;
    }

    public String gePassWordByUserName(String userName) {
        return userMap.get(userName);
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userName = (String) principalCollection.getPrimaryPrincipal();
        Set<String> roles = getRolesByUserName(userName);
        Set<String> permissions = getPermissionsByUserName(userName);
        SimpleAuthorizationInfo authorization = new SimpleAuthorizationInfo();
        authorization.setStringPermissions(permissions);
        authorization.setRoles(roles);
        return authorization;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String userName = (String) authenticationToken.getPrincipal();
        String password = gePassWordByUserName(userName);
        SimpleAuthenticationInfo authentication = new SimpleAuthenticationInfo(userName, password, "myRealm");
        return authentication;
    }
}
