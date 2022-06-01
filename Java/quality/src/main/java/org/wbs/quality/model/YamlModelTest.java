package org.wbs.quality.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * Date:2022/6/1
 */

@ConfigurationProperties(prefix = "test7")
@Component
public class YamlModelTest {

    public int id;
    public String name;

    @Override
    public String toString() {
        return "YamlModelTest{" + "id=" + id + ", name='" + name + '\'' + '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
