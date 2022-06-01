package org.wbs.quality;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wbs.quality.model.YamlModelTest;
import org.yaml.snakeyaml.Yaml;

@SpringBootTest
class QualityApplicationTests {

    /*
    * 全局读取装配
    * */
    @Autowired
    private Environment env;


    @Value("${test1[0].name}")
    private String test1;

    @Value("${test2[0].name}")
    private String test2;

    @Value("${test4.a}")
    private String test3;

    @Value("${test6}")
    private String test4;

    @Autowired
    private YamlModelTest test6;

    /*
    * 数组形式
    * */
    @Test
    public void test1() {
      System.out.println(test1);
    }

    /*
    * 父子节点
    * */
    @Test
    public void test2() {
        System.out.println(test2);
    }

    /*
    * 父子节点
    * */
    @Test
    public void test3() {
        System.out.println(test3);
    }

    /*
    * 配置相加
    * */
    @Test
    public void test4() {
        System.out.println(test4);
    }

    /*
    * 使用全局装配读取
    * */
    @Test
    public void test5(){
        System.out.println(env.getProperty("test1[0].name"));
    }

    @Test
    public void test6() {
        System.out.println(test6.toString());
    }
}
