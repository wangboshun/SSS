package design.代理;

public class SingerProxy implements ISinger {

    private final ISinger target;

    public SingerProxy(ISinger target) {
        this.target = target;
    }

    @Override
    public void sing() {
        System.out.println("向大家问好");
        target.sing();
        System.out.println("唱完了，谢谢大家");
    }
}
