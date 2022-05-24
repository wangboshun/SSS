package design.代理;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class SingerProxyTest {
    public static void main(String[] args) {
        ISinger target=new Singer();
        ISinger proxy=new SingerProxy(target);
        proxy.sing();


        //JDK动态代理
        final Singer target2 = new Singer();
        ISinger proxy2 = (ISinger) Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(), new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("向观众问好");
                        // 执行目标对象方法
                        Object returnValue = method.invoke(target2, args);
                        System.out.println("谢谢大家");
                        return returnValue;
                    }
                });
        proxy2.sing();
    }
}