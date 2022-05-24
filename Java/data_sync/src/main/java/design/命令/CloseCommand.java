package design.命令;

public class CloseCommand implements icommand {

    public CloseCommand(String name){
        this.name = name;
    }

    private String name;

    @Override
    public void execute() {
        String s = this.name;
        receiver r = new receiver();
        r.close(s);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
