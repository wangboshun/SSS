package design.命令;

public class receiver {
    public void open(String name){
        System.out.println(" invoker open "+name);
    }

    public void close(String name){
        System.out.println(" invoker close "+name);
    }

}
