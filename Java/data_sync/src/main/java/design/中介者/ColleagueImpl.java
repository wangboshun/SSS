package design.中介者;

public class ColleagueImpl extends Colleague {

    ColleagueImpl(String name, Mediator m) {
        super(name, m);
        m.addColleague(this);
    }

    @Override
    public void receive(String msg, String name) {
        System.out.println(this.getName() + "接收" + name + "的消息，内容为：" + msg);
    }

    @Override
    public void send(String msg, String... names) {
        StringBuilder l = new StringBuilder();
        for (String name : names) {
            l.append(name).append(",");
        }
        System.out.println(this.getName() + "向" + l + "发送消息，内容为：" + msg);
        this.getM().contact(msg, this, names);
    }
}
