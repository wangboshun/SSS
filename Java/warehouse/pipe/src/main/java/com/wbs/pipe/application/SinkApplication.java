package com.wbs.pipe.application;

import com.wbs.pipe.model.sink.SinkInfoModel;
import org.springframework.stereotype.Service;

/**
 * @author WBS
 * @date 2023/2/23 9:59
 * @desciption SinkApplication
 */
@Service
public class SinkApplication {

    public void query() {

    }

    public void add() {
        SinkInfoModel model = new SinkInfoModel();
        model.setName("test");
        model.setConnect_id("123");
        model.setSink_status(1);
        model.setCreate_time("2022-01-01 22:22:22");

    }

    public void remove() {

    }

    public void update() {

    }
}
