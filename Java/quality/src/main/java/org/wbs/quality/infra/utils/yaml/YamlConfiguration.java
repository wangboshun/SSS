package org.wbs.quality.infra.utils.yaml;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Properties;

/**
 * @author WBS
 * Date:2022/6/9
 */

@Configuration
public class YamlConfiguration {

    @Bean
    public YamlUtils config() {
        Resource app = new ClassPathResource("application.yml");
        YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
        yamlPropertiesFactoryBean.setResources(app);
        Properties properties = yamlPropertiesFactoryBean.getObject();
        return new YamlUtils(properties);
    }
}
