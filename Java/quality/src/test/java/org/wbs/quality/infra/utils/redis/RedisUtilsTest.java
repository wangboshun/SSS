package org.wbs.quality.infra.utils.redis;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.wbs.quality.ApplicationMain;

@SpringBootTest(classes = ApplicationMain.class)
class RedisUtilsTest {

    @Test
    public void String() {
        String key = "aaa";
        org.wbs.quality.infra.utils.redis.RedisUtils.setString(key, "aaaa");
        System.out.println(org.wbs.quality.infra.utils.redis.RedisUtils.getString(key));
    }
}