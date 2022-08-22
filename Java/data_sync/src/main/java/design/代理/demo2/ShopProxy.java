package design.代理.demo2;

/**
 * @author WBS
 * 静态代理
 * Date:2022/8/17
 */

public class ShopProxy implements IShopping{

    private IShopping shop;

    public void setShop(IShopping shop){
        this.shop = shop;
    }

    @Override
    public void Buy() {
        Look();
        shop.Buy();
        Pay();
    }

    public void Look(){
        System.out.println("看看");
    }

    public void Pay(){
        System.out.println("付钱");
    }
}

class ShopMain{
    public static void main(String[] args) {
        ShopProxy shopProxy = new ShopProxy();
        shopProxy.setShop(new Shop());
        shopProxy.Buy();
    }
}
