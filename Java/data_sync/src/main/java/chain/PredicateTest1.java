package chain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/8/30
 */

public class PredicateTest1 {
    public static void main(String[] args) {
        test2();
    }

    public static void test1(){
        List<String> d = new ArrayList<>();
        d.add("a");
        d.add("b");
        d.add("c");

        boolean b=d.removeIf(s-> s.startsWith("a")); //判断过滤

        System.out.println(d);
        System.out.println(b);
    }

    public static void test2(){
        List<String> d = new ArrayList<>();
        d.add("a");
        d.add("b");
        d.add("c");
        d.replaceAll(s->s.toUpperCase());
        System.out.println(d);
    }
}
