package design.命令;

public class OpenCommand implements icommand {

    public OpenCommand(String name){
        this.name = name;
    }

    private String name;

    @Override
    public void execute() {
        String s = this.name;
        receiver r = new receiver();
        r.open(s);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
