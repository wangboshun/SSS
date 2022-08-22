package org.di;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.List;

/**
 * @author WBS
 * Date:2022/8/19
 */

public class DITest {
    private UserService userService;

    //构造函数注入
    //    public DITest(UserService userService){
    //        System.out.println("构造函数注入");
    //        this.userService= userService;
    //    }

    //set注入
    public void setUserService(UserService userService) {
        System.out.println("set注入");
        this.userService = userService;
    }

    public void test() {
        userService.Test();
    }
}

class DIMain {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans4.xml");
        DITest di = (DITest) context.getBean("di");
        di.test();

        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("'Hello World'.concat('!')");
        String message = (String) exp.getValue();
        System.out.println(message);

        int maxValue = (Integer) parser.parseExpression("0x7FFFFFFF").getValue();
        System.out.println(maxValue);


        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSource.setUrl("jdbc:hsqldb:hsql://localhost:");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

//        SpelParserConfiguration config = new SpelParserConfiguration(true, true);
//
//        ExpressionParser parser = new SpelExpressionParser(config);
//
//        Expression expression = parser.parseExpression("list[3]");
//
//        Demo demo = new Demo();
//
//        Object o = expression.getValue(demo);
//        System.out.println(o);
    }
}


class Demo {
    public List<String> list;
}