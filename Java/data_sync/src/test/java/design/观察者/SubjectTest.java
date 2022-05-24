package design.观察者;

class SubjectTest {
    public static void main(String[] args) {
        ConcreteSubject subject=new ConcreteSubject();
        Observer observer1=new ConcreteObserver_1();
        Observer observer2=new ConcreteObserver_2();

        subject.addObserver(observer1);
        subject.addObserver(observer2);
        subject.doSth();
    }
}