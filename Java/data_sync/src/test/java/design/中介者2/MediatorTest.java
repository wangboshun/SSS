package design.中介者2;

class MediatorTest {
    public static void main(String[] args) {
        Mediator m=new ClassMonitor();
        ClassLeader c1=new ClassLeader_1(m);
        ClassLeader c2=new ClassLeader_2(m);
        ClassLeader c3=new ClassLeader_3(m);

        c1.send();
        c2.send();
        c3.send();
    }
}