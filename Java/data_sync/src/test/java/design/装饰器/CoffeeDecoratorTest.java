package design.装饰器;

class CoffeeDecoratorTest {

    public static void main(String[] args) {
        Coffee c=new SimpleCoffee();
        System.out.println(c.getCost()+"  "+c.getOther());

        c=new MilkCoffee(c);
        System.out.println(c.getCost()+"  "+c.getOther());
    }
}