package design.中介者;

import java.util.HashMap;
import java.util.Map;

public class MediatorImpl implements Mediator{
    private Map<String,Colleague> map=new HashMap<>();
    @Override
    public void contact(String msg, Colleague colleague, String... names) {
        for(String name: names){
            Colleague c=map.get(name);
            c.receive(msg,colleague.getName());
        }
    }

    @Override
    public void addColleague(Colleague colleague) {
        this.map.put(colleague.getName(),colleague);
    }
}
