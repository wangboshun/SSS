package design.建造者;

//实现抽象类的所有未实现的方法，具体来说一般是两项任务：组建产品；返回组建好的产品
public interface builder {
    void makerId(String id);
    void makerName(String name);
    product makerUser();
}
