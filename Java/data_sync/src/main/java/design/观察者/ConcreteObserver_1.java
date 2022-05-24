package design.观察者;

public class ConcreteObserver_1 implements Observer{
    @Override
    public void update() {
        System.out.println("ConcreteObserver_1 接收到消息");
    }
}
