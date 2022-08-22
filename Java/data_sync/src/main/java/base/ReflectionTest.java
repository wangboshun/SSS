package base;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author WBS
 * Date:2022/8/14
 */

public class ReflectionTest {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        User u1 = new User(1, "wbs");
        Class c1 = u1.getClass();
        System.out.println(c1.getName());
        for (Field declaredField : c1.getDeclaredFields()) {
            System.out.println("getDeclaredFields:" + declaredField);
        }
        System.out.println("-----------");
        for (Method declaredMethod : c1.getDeclaredMethods()) {
            System.out.println("getDeclaredMethods:" + declaredMethod);
        }
        System.out.println(u1.getId());
        System.out.println("-----------");
        Method m1= c1.getDeclaredMethod("setId",int.class);
        m1.setAccessible(true);
        m1.invoke(u1,1234);
        System.out.println(u1.getId());

    }
}

class User {
    public String age;
    private int id;
    private   String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    private void test1() {

    }
}
