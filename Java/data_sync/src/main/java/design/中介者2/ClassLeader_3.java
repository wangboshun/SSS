package design.中介者2;

public class ClassLeader_3 implements ClassLeader {

    private final Mediator m;

    public ClassLeader_3(Mediator m) {
//        super();
        this.m = m;
        m.redister("ClassLeader_3", this);
    }

    @Override
    public void job() {
        System.out.println("ClassLeader_3 job");
    }

    @Override
    public void send() {
        System.out.println("ClassLeader_3 say:no my job");
        m.command("ClassLeader_1");
        m.command("ClassLeader_2");
    }
}
