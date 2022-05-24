package design.命令;

import java.util.ArrayList;
import java.util.List;

public class invoker {
    public List<icommand> commands = new ArrayList<>();

    public invoker(icommand c) {
        this.commands.add(c);
    }

    public invoker(List<icommand> commands) {
        this.commands.addAll(commands);
    }

    public void action() {
        for (icommand command : commands) {
            command.execute();
        }
    }
}
