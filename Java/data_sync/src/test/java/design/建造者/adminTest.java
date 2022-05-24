package design.建造者;

import com.google.gson.Gson;

class adminTest {
    public static void main(String[] args) {
        director a = new director();

        //不同的具体构建者

        a.setBuilder(new concreteBuilder_1());
        product u1 = a.makerUser("1", "wbs");
        System.out.println(new Gson().toJson(u1));

        a.setBuilder(new concreteBuilder_2());
        product u2 = a.makerUser("1", "wbs");
        System.out.println(new Gson().toJson(u2));
    }
}