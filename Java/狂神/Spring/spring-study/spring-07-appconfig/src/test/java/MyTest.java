import com.blue.config.BlueConfig;
import com.blue.pojo.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MyTest {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(BlueConfig.class);
        User getuser = context.getBean("getUser", User.class);
        System.out.println(getuser.getName());
    }
}
