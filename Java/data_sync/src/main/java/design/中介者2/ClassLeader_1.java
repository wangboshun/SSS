package design.中介者2;

public class ClassLeader_1 implements ClassLeader {

    private final Mediator m;

    public ClassLeader_1(Mediator m) {
//        super();
        this.m = m;
        m.redister("ClassLeader_1", this);
    }

    @Override
    public void job() {
        System.out.println("ClassLeader_1 job");
    }

    @Override
    public void send() {
        System.out.println("ClassLeader_1 say:no my job");
        m.command("ClassLeader_2");
    }
}
