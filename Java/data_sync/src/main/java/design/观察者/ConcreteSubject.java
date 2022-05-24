package design.观察者;

public class ConcreteSubject extends Subject {
    public void doSth() {
        super.notifyObservers();
    }
}
