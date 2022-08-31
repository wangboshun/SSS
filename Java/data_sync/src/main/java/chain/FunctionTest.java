package chain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author WBS
 * Date:2022/8/30
 */

public class FunctionTest {
    public static void main(String[] args) {
        test3();
    }

    public static void test1() {
        Map<String, Integer> map = new HashMap<>();
        Integer c = map.computeIfAbsent("hello", s -> s.length()); //计算长度
        System.out.println(c);
        System.out.println(map);
    }

    public static void test2() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 1);
        map.put("c", 1);
        map.put("d", 1);

        map.replaceAll((k, v) -> { //替换
            if (k.equals("a")) {
                return 2;
            }
            return v;
        });

        System.out.println(map);
    }

    public static void test3() {
        //利用java8新特性stream流
        List<Map<String, Integer>> l = new ArrayList<>(10);

        Map<String, Integer> a = new HashMap<>();
        a.put("a", 1);

        Map<String, Integer> b = new HashMap<>();
        b.put("b", 2);

        Map<String, Integer> c = new HashMap<>();
        c.put("c", 3);

        Map<String, Integer> d = new HashMap<>();
        c.put("d", 3);

        Map<String, Integer> e = new HashMap<>();
        c.put("e", 3);

        Map<String, Integer> f = new HashMap<>();
        c.put("f", 3);

        l.add(a);
        l.add(b);
        l.add(c);
        l.add(d);
        l.add(e);
        l.add(f);

        l.forEach(s -> {
            System.out.println(s);
        });

//        l.stream().map(x -> {
//
//            if (x.containsKey("a")) {  //替换key
//                x.put("aaa", x.remove("a"));
//            }
//
//            return x;
//        }).collect(Collectors.toList());

        l.parallelStream().map(x -> { //并行方式

            System.out.println("thread-" + Thread.currentThread().getName());
            if (x.containsKey("a")) {  //替换key
                x.put("aaa", x.remove("a"));
            }
            return x;
        }).collect(Collectors.toList());

        l.forEach(s -> {
            System.out.println(s);
        });


        l.forEach(s -> {
            System.out.println(s);
        });


        l.replaceAll(x -> {

            if (x.containsKey("b")) { //替换value
                x.put("b", 111);
            }

            return x;
        });

        l.forEach(s -> {
            System.out.println(s);
        });


    }
}
