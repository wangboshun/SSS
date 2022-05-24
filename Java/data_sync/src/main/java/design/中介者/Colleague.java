package design.中介者;

public abstract class Colleague {
    private final Mediator m;
    private final String name;

    public String getName() {
        return name;
    }

    public Mediator getM() {
        return m;
    }

    Colleague(String name, Mediator m) {
        this.name = name;
        this.m = m;
    }

    public abstract void receive(String msg, String name);

    public abstract void send(String msg, String... name);
}
