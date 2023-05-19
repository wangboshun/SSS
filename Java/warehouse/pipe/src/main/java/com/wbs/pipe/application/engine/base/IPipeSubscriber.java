package com.wbs.pipe.application.engine.base;

/**
 * @author WBS
 * @date 2023/5/19 9:54
 * @desciption IPipeSubscriber
 */
public interface IPipeSubscriber  {
    public void eventBusReceive(String message);
    public void rabbitMqReceive(String message);
    public void kafkaReceive(String message);
    public void run(String message);
}
