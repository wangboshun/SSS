package design.观察者;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {
    private final List<Observer> list = new ArrayList<Observer>();

    public void addObserver(Observer observer) {
        list.add(observer);
    }

    public void removeObserver(Observer observer) {
        list.remove(observer);
    }

    public void notifyObservers() {
        System.out.println("通知所有");
        for (Observer observer : list) {
            observer.update();
        }
    }
}
