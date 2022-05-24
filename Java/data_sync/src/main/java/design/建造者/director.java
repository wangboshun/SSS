package design.建造者;

//指挥者
//负责调用适当的建造者来组建产品，指挥类一般不与产品类发生依赖关系，与指挥类直接交互的是建造者类。一般来说，指挥类被用来封装程序中易变的部分
public class director {
    private builder a;
    public void setBuilder(builder a){
        this.a=a;
    }
    public product makerUser(String id, String name){
        a.makerId(id);
        a.makerName(name);
        return a.makerUser();
    }
}
