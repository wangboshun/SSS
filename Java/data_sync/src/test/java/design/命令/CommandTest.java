package design.命令;

import java.util.ArrayList;
import java.util.List;

class CommandTest {
    public static void main(String[] args) {
        icommand open = new OpenCommand("aaa");
        icommand close = new CloseCommand("bbb");
        List<icommand> list = new ArrayList<>();
        list.add(open);
        list.add(close);

        invoker i = new invoker(list);
        i.action();
    }
}