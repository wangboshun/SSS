package com.zny.pipe.component.sink;

import com.google.gson.Gson;
import com.zny.common.enums.DbTypeEnum;
import com.zny.common.json.GsonEx;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/10/12
 * mysql目的服务类
 */

@Component
@RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "MySQL_Queue", durable = "true"), exchange = @Exchange(value = "Pipe_Exchange"), key = "MySQL_RoutKey")})
public class MySqlSink extends SinkAbstract implements InitializingBean {

    @RabbitHandler
    public void onMessage(String message) {
        System.out.println("MySql_Queue --->接收消息:\r\n" + message);
        if (sinkStatus == 0) {
            return;
        }
        Gson gson = GsonEx.getInstance();
        List<Map<String, Object>> list = new ArrayList<>();
        list = gson.fromJson(message, list.getClass());
        setData(list);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SinkFactory.register(DbTypeEnum.MySQL, this);
    }
}
