import com.blue.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest {
    public static void main(String[] args) {
        ApplicationContext  context = new ClassPathXmlApplicationContext("applicationContext.xml");
        System.out.println(context);
        //动态代理代理的是接口
        UserService userService = context.getBean("userService", UserService.class);

        userService.add();
    }
}
