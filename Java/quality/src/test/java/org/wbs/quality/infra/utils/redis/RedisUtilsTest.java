package org.wbs.quality.infra.utils.redis;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.wbs.quality.ApplicationMain;
import org.wbs.quality.infra.utils.RedisUtils;

@SpringBootTest(classes = ApplicationMain.class)
class RedisUtilsTest {

    @Test
    public void String() {
        String key = "aaa";
        RedisUtils.setString(key, "aaaa");
        System.out.println(RedisUtils.getString(key));
    }
}