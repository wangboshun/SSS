package chain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/8/30
 */

public class ConsumerTest1 {

    public static void main(String[] args) {
        test2();
    }

    public static void test1() {
        List<String> d = new ArrayList<>();
        d.add("a");
        d.add("b");
        d.add("c");
        d.forEach(s -> {
            System.out.println(s);
        });
    }

    public static void test2() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 1);
        map.put("c", 1);
        map.put("d", 1);

        map.forEach((k, v) -> {
            System.out.println(k + "," + v);
        });
    }
}
