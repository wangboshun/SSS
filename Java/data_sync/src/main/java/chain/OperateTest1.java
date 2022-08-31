package chain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WBS
 * Date:2022/8/30
 */

public class OperateTest1 {
    public static void main(String[] args) {
        List<String> d = new ArrayList<>();
        d.add("a");
        d.add("b");
        d.add("c");

        d.replaceAll(s -> s.toUpperCase());
        System.out.println(d);

        String s = d.stream().reduce("Hello",(a, b) -> a + "," + b);  //叠加

        System.out.println(s);

    }
}
