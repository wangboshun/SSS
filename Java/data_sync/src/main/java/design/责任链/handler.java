package design.责任链;

public abstract class handler {
    public int maxDays;
    private handler next;

    protected handler(int maxDays) {
        this.maxDays = maxDays;
    }

    public void setNextHandler(handler h) {
        this.next = h;
    }

    protected void request(int day) {
        if (day <= maxDays) {
            doSth(day);
        } else {
            if (next != null) {
                next.request(day);
            } else {
                System.out.println("最后一级了");
            }
        }
    }

    protected abstract void doSth(int day);
}
