package org.application;

import org.model.UserInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WBS
 * Date:2022/8/17
 */

@Configuration
public class UserInfoService {

    @Bean
    public UserInfo getUserInfo() {
        return new UserInfo();
    }
}
