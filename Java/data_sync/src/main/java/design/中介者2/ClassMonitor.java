package design.中介者2;

import java.util.HashMap;
import java.util.Map;

public class ClassMonitor implements Mediator {

    private final Map<String, ClassLeader> map = new HashMap<>();

    @Override
    public void redister(String name, ClassLeader l) {
        map.put(name, l);
    }

    @Override
    public void command(String name) {
        map.get(name).job();
    }
}
