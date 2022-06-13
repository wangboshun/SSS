package org.wbs.quality;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.wbs.quality.dao.TestDao;
import org.wbs.quality.infra.utils.YamlUtils;
import org.wbs.quality.model.Test1;
import org.wbs.quality.model.YamlModelTest;

@SpringBootTest(classes = ApplicationMain.class)
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

    /*
     * 封装类
     * */
    @Autowired
    private YamlModelTest test6;

    @Autowired
    private TestDao testDao;

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
    public void test5() {
        System.out.println(env.getProperty("test1[0].name"));
    }

    /*
     * 封装类
     * */
    @Test
    public void test6() {
        System.out.println(test6.toString());
    }

    @Test
    public void test7() {
        Test1 m = testDao.getById("1");
        System.out.println(m.toString());
    }

    /*
     * 根据帮助类获取yml配置，不需要装配注入
     * */
    @Test
    public void test8() {
        String v1 = YamlUtils.getString("test1[0].id");
        String v2 = YamlUtils.getString("test1[0].name");
        String v3 = YamlUtils.getString("test1[1].name");
        System.out.println(v1 + " " + v2 + " " + v3);
    }
}
