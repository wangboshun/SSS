package org.wbs.quality.infra.utils.yaml;

import java.util.Properties;

/**
 * @author WBS
 * Date:2022/6/9
 */

public class YamlUtils {

    private static Properties properties;

    public YamlUtils(Properties properties) {
        YamlUtils.properties = properties;
    }

    public static String getString(String key) {
        return properties.getProperty(key);
    }

    public static Integer getInteger(String key) {
        return Integer.valueOf(getString(key));
    }
}
