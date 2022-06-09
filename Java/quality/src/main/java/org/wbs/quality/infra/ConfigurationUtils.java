package org.wbs.quality.infra;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.wbs.quality.infra.utils.RedisUtils;
import org.wbs.quality.infra.utils.YamlUtils;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Properties;

/**
 * @author WBS
 * Date:2022/6/9
 */

@Configuration
public class ConfigurationUtils {

    /**
     * YAML注入
     */
    @Bean("yamlConfig")
    public YamlUtils yamlConfig() {
        Resource app = new ClassPathResource("application.yml");
        YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
        yamlPropertiesFactoryBean.setResources(app);
        Properties properties = yamlPropertiesFactoryBean.getObject();
        return new YamlUtils(properties);
    }

    /**
     * REDIS注入
     */
    @Bean
    @DependsOn("yamlConfig")  //需要依赖yamlConfig
    public RedisUtils redisConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        String host = YamlUtils.getString("spring.redis.host");
        String password = YamlUtils.getString("spring.redis.password");
        Integer port = YamlUtils.getInteger("spring.redis.port");
        Integer database = YamlUtils.getInteger("spring.redis.database");
        //连接超时（默认2000ms）
        JedisPool pool = new JedisPool(jedisPoolConfig, host, port, 2000, password, database);
        return new RedisUtils(pool);
    }


}
