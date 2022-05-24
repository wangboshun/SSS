package design.策略模式;

class StrategyTest {
    public static void main(String[] args) {
        Context c=new Context(new OperateAdd());
        int result=c.excuteOperate(111,123);
        System.out.println(result);

        c=new Context(new OperateSub());
        result=c.excuteOperate(111,123);
        System.out.println(result);



        Context2 c2=new Context2("+");
        result=c2.excuteOperate(111,123);
        System.out.println(result);

        c2=new Context2("-");
        result=c2.excuteOperate(111,123);
        System.out.println(result);
    }
}