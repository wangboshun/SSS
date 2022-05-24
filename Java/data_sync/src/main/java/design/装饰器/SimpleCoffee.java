package design.装饰器;

public class SimpleCoffee implements Coffee{
    @Override
    public double getCost() {
        return 1;
    }

    @Override
    public String getOther() {
        return "Coffee";
    }
}
