package org.wbs.quality.infra.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author WBS
 * Date:2022/6/9
 */

public class RedisUtils {

    private static Jedis redis;

    public RedisUtils(JedisPool pool) {
        RedisUtils.redis = pool.getResource();
    }

    public static void setString(String key, String value) {
        redis.set(key, value);
    }

    public static String getString(String key) {
        return redis.get(key);
    }
}
