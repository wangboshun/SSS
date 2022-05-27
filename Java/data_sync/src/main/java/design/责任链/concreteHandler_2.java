package design.责任链;

public class concreteHandler_2 extends handler{

    protected concreteHandler_2(int maxDays) {
        super(maxDays);
    }

    @Override
    protected void doSth(int day) {
        System.out.println("concreteHandler_2 处理 day:"+day+" "+maxDays);
    }
}
