package design.策略模式;

//结合简单工厂模式
public class Context2 {
    private Strategy strategy;

    public Context2(String type) {
        switch (type) {
            case "+":
                strategy = new OperateAdd();
                break;
            case "-":
                strategy = new OperateSub();
                break;
            default:
                break;
        }
    }

    public int excuteOperate(int num1, int num2) {
        return strategy.doSth(num1, num2);
    }
}
