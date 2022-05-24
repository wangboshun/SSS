package design.中介者2;

public class ClassLeader_2 implements ClassLeader {

    private final Mediator m;

    public ClassLeader_2(Mediator m) {
//        super();
        this.m = m;
        m.redister("ClassLeader_2", this);
    }

    @Override
    public void job() {
        System.out.println("ClassLeader_2 job");
    }

    @Override
    public void send() {
        System.out.println("ClassLeader_2 say:no my job");
        m.command("ClassLeader_1");
    }
}
