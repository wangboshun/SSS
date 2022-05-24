package design.中介者;

public interface Mediator {
    void contact(String msg,Colleague colleague,String ... names);
    void addColleague(Colleague colleague);
}
