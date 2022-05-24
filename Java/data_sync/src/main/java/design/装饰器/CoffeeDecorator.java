package design.装饰器;

public abstract class CoffeeDecorator implements Coffee {
    protected final Coffee coffee;

    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }

    public double getCost() {
        return this.coffee.getCost();
    }

    public String getOther() {
        return coffee.getOther();
    }
}
