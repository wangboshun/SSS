package design.中介者;

class MediatorTest {
    public static void main(String[] args) {
        Mediator m=new MediatorImpl();
        Colleague c1=new ColleagueImpl("AAA",m);
        Colleague c2=new ColleagueImpl("BBB",m);
        Colleague c3=new ColleagueImpl("CCC",m);
        Colleague c4=new ColleagueImpl("DDD",m);

//        m.addColleague(c1);
//        m.addColleague(c2);
//        m.addColleague(c3);
//        m.addColleague(c4);

        c1.send("test",c2.getName(),c3.getName(),c4.getName());
        c2.send("test2",c3.getName(),c4.getName());
    }
}