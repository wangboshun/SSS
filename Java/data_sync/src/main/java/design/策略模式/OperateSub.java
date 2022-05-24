package design.策略模式;

public class OperateSub implements Strategy{
    @Override
    public int doSth(int num1, int num2) {
        return num1-num2;
    }
}
