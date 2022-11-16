package com.zny.pipe.component.queue;

import com.google.gson.Gson;
import com.zny.common.json.GsonEx;
import com.zny.pipe.component.base.TransformAbstract;
import com.zny.pipe.model.MessageBodyModel;
import org.springframework.stereotype.Component;

/**
 * @author WBS
 * Date 2022-11-15 9:34
 * QueueBase
 */

@Component
public abstract class QueueBase {

    private final TransformAbstract transformAbstract;

    public QueueBase(TransformAbstract transformAbstract) {
        this.transformAbstract = transformAbstract;
    }

    public void process(String message) {
        Gson gson = GsonEx.getInstance();
        MessageBodyModel body = gson.fromJson(message, MessageBodyModel.class);
        transformAbstract.transform(body);
    }
}
