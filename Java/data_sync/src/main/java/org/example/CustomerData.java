package org.example;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
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

        Log log = LogFactory.get();
        log.info("Id:" + id + "--->" + json);
       System.out.println("Id:" + id + "--->" + json);
    }
}