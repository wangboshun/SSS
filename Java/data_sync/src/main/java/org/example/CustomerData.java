package org.example;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class CustomerData extends Thread {
    private final List<Map<String, Object>> list;

    public CustomerData(List<Map<String, Object>> data) {
        this.list = data;
    }

    @Override
    public void run() {
        Gson g = new Gson();
        String json = g.toJson(list);
        long id = Thread.currentThread().getId();
        System.out.println("Id:" + id + "--->" + json);
    }
}
