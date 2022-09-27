package com.zny.user.application;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zny.common.enums.ResourceEnum;
import com.zny.common.enums.UserTypeEnum;
import com.zny.common.resource.ResourceApplication;
import com.zny.common.resource.ResourceModel;
import com.zny.common.utils.DateUtils;
import com.zny.user.mapper.UserMapper;
import com.zny.user.model.user.UserModel;
import com.zny.user.model.user.UserTreeModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author WBS
 * Date:2022/9/2
 */

@Service
@DS("main")
public class UserApplication extends ServiceImpl<UserMapper, UserModel> {

    private final ResourceApplication resourceApplication;

    public UserApplication(ResourceApplication resourceApplication) {
        this.resourceApplication = resourceApplication;
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     */
    public Map<String, String> login(String username, String password) {
        QueryWrapper<UserModel> wrapper = new QueryWrapper<UserModel>();
        wrapper.eq("user_name", username);
        wrapper.eq("password", SecureUtil.md5(password));
        UserModel model = this.getOne(wrapper);
        if (model != null) {
            StpUtil.login(model.getId());
            StpUtil.getSession().set("userType", model.getUser_type());
            StpUtil.getSession().set("userId", model.getId());
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            Map<String, String> map = new HashMap<String, String>(1);
            map.put(tokenInfo.getTokenName(), tokenInfo.getTokenValue());
            return map;
        }
        return null;
    }

    /**
     * 添加用户
     *
     * @param username 用户名
     * @param password 密码
     * @param userType 用户类型
     * @param parentId 父级id
     */
    public SaResult addUser(String username, String password, Integer userType, String parentId) {
        QueryWrapper<UserModel> wrapper = new QueryWrapper<UserModel>();
        wrapper.eq("user_name", username);
        UserModel model = this.getOne(wrapper);
        if (model != null) {
            return SaResult.error("用户名已存在！");
        }
        UserModel userModel = new UserModel();
        userModel.setId(UUID.randomUUID().toString());
        userModel.setUser_name(username);
        if (StringUtils.isNotBlank(parentId)) {
            userModel.setParent_id(parentId);
        }
        if (userType != null) {
            userModel.setUser_type(userType);
        }
        else {
            userModel.setUser_type(UserTypeEnum.COMMON.getIndex());
        }

        userModel.setCreate_time(DateUtils.dateToStr(LocalDateTime.now()));
        userModel.setPassword(SecureUtil.md5(password));
        if (save(userModel)) {
            return SaResult.ok("添加用户成功！");
        }
        else {
            return SaResult.error("添加用户失败！");
        }
    }

    /**
     * 查询用户列表
     *
     * @param userId    用户id
     * @param userName  用户名
     * @param pageIndex 页码
     * @param pageSize  分页大小
     */
    public Map<String, Object> getUserList(String userId, String userName, Integer pageIndex, Integer pageSize) {
        if (pageSize == null) {
            pageSize = 10;
        }
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        QueryWrapper<UserModel> wrapper = new QueryWrapper<UserModel>();
        wrapper.eq(StringUtils.isNotBlank(userId), "id", userId);
        wrapper.eq(StringUtils.isNotBlank(userName), "user_name", userName);
        Page<UserModel> page = new Page<>(pageIndex, pageSize);
        Page<UserModel> result = this.page(page, wrapper);
        Map<String, Object> map = new HashMap<>(4);
        map.put("total", result.getTotal());
        map.put("rows", result.getRecords());
        map.put("pages", result.getPages());
        map.put("current", result.getCurrent());
        return map;
    }

    /**
     * 查询用户树
     *
     * @param userId 用户id
     */
    public List<UserTreeModel> getUserTree(String userId) {
        List<UserTreeModel> list = new ArrayList<>();

        //查找根目录
        if (userId == null) {
            QueryWrapper<UserModel> wrapper = new QueryWrapper<UserModel>();

            //查找没有父级id的用户
            wrapper.isNull("parent_id");
            List<UserModel> menuList = this.list(wrapper);
            for (UserModel user : menuList) {
                list.add(getChildren(user.getId(), user.getUser_name(), 1));
            }
        }
        else {
            QueryWrapper<UserModel> wrapper = new QueryWrapper<UserModel>();
            wrapper.eq(StringUtils.isNotBlank(userId), "id", userId);
            UserModel user = this.getOne(wrapper);
            list.add(getChildren(user.getId(), user.getUser_name(), 1));
        }
        return list;
    }

    /**
     * 获取子级
     *
     * @param userId 用户id
     * @param level  树形等级
     */
    private UserTreeModel getChildren(String userId, String userName, Integer level) {
        UserTreeModel tree = new UserTreeModel();
        QueryWrapper<UserModel> wrapper = new QueryWrapper<UserModel>();
        wrapper.eq(StringUtils.isNotBlank(userId), "parent_id", userId);
        List<UserModel> children = this.list(wrapper);
        tree.setId(userId);
        tree.setLelvel(level);
        tree.setUser_name(userName);
        if (children.size() > 0) {
            for (UserModel user : children) {
                tree.setChildren(getChildren(user.getId(), user.getUser_name(), level + 1));
            }
        }
        return tree;
    }

    /**
     * 删除用户
     *
     * @param id 用户id
     */
    public SaResult deleteUser(String id) {
        QueryWrapper<UserModel> wrapper = new QueryWrapper<UserModel>();
        wrapper.eq("id", id);
        UserModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResult.error("用户不存在！");
        }
        if (removeById(id)) {
            return SaResult.ok("删除用户成功！");
        }
        else {
            return SaResult.error("删除用户失败！");
        }
    }

    /**
     * 更新用户信息
     *
     * @param id       用户id
     * @param username 用户名
     * @param password 用户密码
     * @param userType 用户类型
     * @param parentId 父级id
     */
    public SaResult updateUser(String id, String username, String password, Integer userType, String parentId) {
        QueryWrapper<UserModel> wrapper = new QueryWrapper<UserModel>();
        wrapper.eq("id", id);
        UserModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResult.error("用户不存在！");
        }
        if (userType != null) {
            model.setUser_type(userType);
        }
        if (StringUtils.isNotBlank(parentId)) {
            model.setParent_id(parentId);
        }
        model.setPassword(SecureUtil.md5(password));
        model.setUser_name(username);
        if (updateById(model)) {
            return SaResult.ok("更新用户信息成功！");
        }
        else {
            return SaResult.error("删除用户信息失败！");
        }
    }

    /**
     * 根据角色获取用户
     *
     * @param roleId 角色id
     */
    public List<UserModel> getUserByRole(String roleId) {
        List<ResourceModel> resourceList = resourceApplication.getResourceList(roleId, ResourceEnum.ROLE.getIndex(), ResourceEnum.USER.getIndex());
        return getUserByResourceModel(resourceList);
    }

    /**
     * 根据资源映射获取用户
     *
     * @param list 资源列表
     */
    private List<UserModel> getUserByResourceModel(List<ResourceModel> list) {
        List<UserModel> userList = new ArrayList<UserModel>();
        for (ResourceModel resourceModel : list) {
            UserModel userModel = this.getById(resourceModel.getSlave_id());
            userList.add(userModel);
        }
        return userList;
    }

    /**
     * 绑定菜单到角色
     *
     * @param roleId 角色id
     * @param userId 用户id
     */
    public SaResult bindUserByRole(String roleId, String[] userId) {
        return resourceApplication.addResource(roleId, ResourceEnum.ROLE.getIndex(), userId, ResourceEnum.USER.getIndex());
    }

    /**
     * 解绑用户到角色
     *
     * @param roleId 角色id
     * @param userId id
     */
    public SaResult unBindUserByRole(String roleId, String[] userId) {
        return resourceApplication.deleteResource(null, roleId, ResourceEnum.ROLE.getIndex(), userId, ResourceEnum.USER.getIndex());
    }
}
