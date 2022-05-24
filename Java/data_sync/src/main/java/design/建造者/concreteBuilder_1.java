package design.建造者;

//具体建造者
//引入抽象建造者的目的，是为了将建造的具体过程交与它的子类来实现。这样更容易扩展。一般至少会有两个抽象方法，一个用来建造产品，一个是用来返回产品。

public class concreteBuilder_1 implements builder {
    product u = new product();

    @Override
    public void makerId(String id) {
        u.setId(id+"_1");
    }

    @Override
    public void makerName(String name) {
        u.setName(name+"_1");
    }

    @Override
    public product makerUser() {
        return u;
    }
}
