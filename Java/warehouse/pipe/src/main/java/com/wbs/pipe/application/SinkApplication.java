package com.wbs.pipe.application;

import cn.hutool.json.JSONUtil;
import com.mongodb.client.result.DeleteResult;
import com.wbs.pipe.model.sink.SinkInfoModel;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SinkApplication {

    private MongoTemplate _mongoTemplate;

    public SinkApplication(MongoTemplate mongoTemplate) {
        _mongoTemplate = mongoTemplate;
    }

    public void query() {
        List<SinkInfoModel> list = _mongoTemplate.findAll(SinkInfoModel.class);
        System.out.println(JSONUtil.toJsonPrettyStr(list));
    }

    public void add() {
        SinkInfoModel model = new SinkInfoModel();
        model.setName("test");
        model.setConnect_id("123");
        model.setSink_status(1);
        model.setCreate_time("2022-01-01 22:22:22");
        SinkInfoModel insert = _mongoTemplate.insert(model);
        System.out.println(JSONUtil.toJsonPrettyStr(insert));
    }

    public void remove() {
        Query query = Query.query(Criteria.where("name").is("12222"));
        DeleteResult remove = _mongoTemplate.remove(query, SinkInfoModel.class);
    }

    public void update() {
        Query query = Query.query(Criteria.where("name").is("test"));
        Update update = new Update().set("name", "12222");
       _mongoTemplate.updateFirst(query, update,SinkInfoModel.class);
    }
}
