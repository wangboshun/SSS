package design.代理.demo2;

/**
 * @author WBS
 * Date:2022/8/17
 */

public class Shop implements IShopping {
    @Override
    public void Buy() {
        System.out.println("买东西");
    }
}
