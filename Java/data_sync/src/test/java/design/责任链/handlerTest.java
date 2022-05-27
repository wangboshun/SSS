package design.责任链;

class handlerTest {
    public static void main(String[] args) {
        handler h1 = new concreteHandler_1(5);
        handler h2 = new concreteHandler_2(10);

        h1.setNextHandler(h2);

        h1.request(3);
        h1.request(6);
        h1.request(11);
//        h1.request(15);

    }
}