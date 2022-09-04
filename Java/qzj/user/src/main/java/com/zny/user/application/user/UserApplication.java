package com.zny.user.application.user;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zny.common.utils.DateUtils;
import com.zny.user.mapper.UserMapper;
import com.zny.user.model.UserModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author WBS
 * Date:2022/9/2
 */

@Service
public class UserApplication extends ServiceImpl<UserMapper, UserModel> {

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     */
    public String login(String username, String password) {
        QueryWrapper<UserModel> wrapper = new QueryWrapper<UserModel>();
        wrapper.eq("user_name", username);
        wrapper.eq("password", SecureUtil.md5(password));
        UserModel model = this.getOne(wrapper);
        if (model != null) {
            StpUtil.login(model.id);
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            return tokenInfo.getTokenValue();
        }
        return null;
    }

    /**
     * 添加用户
     *
     * @param username 用户名
     * @param password 密码
     */
    public SaResult addUser(String username, String password) {
        QueryWrapper<UserModel> wrapper = new QueryWrapper<UserModel>();
        wrapper.eq("user_name", username);
        UserModel model = this.getOne(wrapper);
        if (model != null) {
            return SaResult.error("用户名已存在！");
        }
        UserModel userModel = new UserModel();
        userModel.setId(UUID.randomUUID().toString());
        userModel.setUser_name(username);
        userModel.setCreate_time(DateUtils.dateToStr(LocalDateTime.now()));
        userModel.setPassword(SecureUtil.md5(password));
        if (save(userModel)) {
            return SaResult.ok("添加用户成功！");
        } else {
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
        Map<String, Object> map = new HashMap<>();
        map.put("total", result.getTotal());
        map.put("rows", result.getRecords());
        map.put("pages", result.getPages());
        map.put("current", result.getCurrent());
        return map;
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
        } else {
            return SaResult.error("删除用户失败！");
        }
    }

    /**
     * 更新用户信息
     *
     * @param id       用户id
     * @param username 用户名
     * @param password 用户密码
     */
    public SaResult updateUser(String id, String username, String password) {
        QueryWrapper<UserModel> wrapper = new QueryWrapper<UserModel>();
        wrapper.eq("id", id);
        UserModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResult.error("用户不存在！");
        }
        model.setPassword(SecureUtil.md5(password));
        model.setUser_name(username);
        if (updateById(model)) {
            return SaResult.ok("更新用户信息成功！");
        } else {
            return SaResult.error("删除用户信息失败！");
        }
    }
}