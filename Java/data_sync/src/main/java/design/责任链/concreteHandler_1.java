package design.责任链;

public class concreteHandler_1 extends handler{

    protected concreteHandler_1(int maxDays) {
        super(maxDays);
    }

    @Override
    protected void doSth(int day) {
        System.out.println("concreteHandler_1 处理 day:"+day);
    }
}
