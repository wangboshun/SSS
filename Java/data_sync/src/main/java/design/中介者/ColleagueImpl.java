package design.中介者;

public class ColleagueImpl extends Colleague {

    ColleagueImpl(String name, Mediator m) {
        super(name, m);
//        m.addColleague(this);
    }

    @Override
    public void receive(String msg, String name) {
        System.out.println("我" + this.getName() + " 被" + name + " 联系了， " + "信息为:" + msg );
    }

    @Override
    public void send(String msg, String... names) {
        System.out.print("我 " + this.getName() + " 向 ");
        for (String n : names){
            System.out.print(n + ",");
        }
        System.out.println(" 发 " + msg + " 信息");
        this.getM().contact(msg,this, names);
    }
}
