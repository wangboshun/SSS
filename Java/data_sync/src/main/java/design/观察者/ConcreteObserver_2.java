package design.观察者;

public class ConcreteObserver_2 implements Observer{
    @Override
    public void update() {
        System.out.println("ConcreteObserver_2 接收到消息");
    }
}
