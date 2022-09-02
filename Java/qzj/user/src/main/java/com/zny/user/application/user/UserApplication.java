package com.zny.user.application.user;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zny.user.mapper.UserMapper;
import com.zny.user.model.UserModel;
import org.springframework.stereotype.Service;

/**
 * @author WBS
 * Date:2022/9/2
 */

@Service
public class UserApplication extends ServiceImpl<UserMapper, UserModel> {
    public String login(String username, String password) {
        QueryWrapper<UserModel> wrapper = new QueryWrapper<UserModel>();
        wrapper.eq("user_name", username);
        wrapper.eq("password", password);
        UserModel model = this.getOne(wrapper);
        if (model != null) {
            StpUtil.login(model.id);
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            return tokenInfo.getTokenValue();
        }
        return null;
    }
}
