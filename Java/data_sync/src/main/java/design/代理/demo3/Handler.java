package design.代理.demo3;

import design.代理.demo2.IShopping;
import design.代理.demo2.Shop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author WBS
 * 动态代理
 * Date:2022/8/17
 */

public class Handler implements InvocationHandler {

    private Object target;

    public void setTarget(Object target) {
        this.target = target;
    }

    public Object getProxy() {
        return java.lang.reflect.Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Look();
        return method.invoke(target, args);
    }

    private void Look(){
        System.out.println("看看");
    }
}

class HandlerMain {
    public static void main(String[] args) {
        IShopping shop = new Shop();
        Handler handler = new Handler();
        handler.setTarget(shop);
        IShopping proxy = (IShopping) handler.getProxy();
        proxy.Buy();
    }
}
