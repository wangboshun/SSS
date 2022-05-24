package design.策略模式;

public class Context {
    private Strategy strategy;

    public Context(Strategy strategy){
        this.strategy=strategy;
    }

    public int excuteOperate(int num1, int num2){
        return strategy.doSth(num1, num2);
    }
}
