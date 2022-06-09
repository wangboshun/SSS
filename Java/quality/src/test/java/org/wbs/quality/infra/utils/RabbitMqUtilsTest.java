package org.wbs.quality.infra.utils;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wbs.quality.ApplicationMain;

@SpringBootTest(classes = ApplicationMain.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RabbitMqUtilsTest {

    @Autowired
    private RabbitMqUtils mqUtils;

    /*
     * 一对一
     * */
    @Test
    @Order(1)
    void sendForWork() {
        System.out.println("send");
        mqUtils.sendMsgForWork("work-test-1", "555");
    }

    @Test
    @Order(2)
    void receiveForWork1() {
        System.out.println("receiveForWork1");
        mqUtils.receiveMsgForWork("work-test-1");
    }

    @Test
    @Order(2)
    void receiveForWork2() {
        System.out.println("receiveForWork2");
        mqUtils.receiveMsgForWork("work-test-1");
    }

    /*
     * 发布者发布key
     * 消费者只消费拥有的key
     * 如下面，发布者发布xxx、yyy，消费者订阅ccc，所以获取不到任何数据
     * */
    @Test
    public void topicTest1() {
        mqUtils.sendMsgForTopic("topicTest1", "xxx.*", "999");
        mqUtils.sendMsgForTopic("topicTest1", "yyy.*", "999");
        mqUtils.receiveMsgForTopic("topicTest1", new String[]{"ccc.*"});
    }

    /*
     * 发布者发布key
     * 消费者只消费拥有的key
     * 如下面，发布者发布ccc、ddd，消费者订阅ccc、ddd，所以能获取数据
     * */
    @Test
    public void topicTest2() {
        mqUtils.sendMsgForTopic("topicTest1", "ccc.*", "999");
        mqUtils.sendMsgForTopic("topicTest1", "ddd.*", "999");
        mqUtils.receiveMsgForTopic("topicTest1", new String[]{"ccc.*", "ddd.*"});
    }

    @Test
    public void directTest1() {
        mqUtils.sendMsgForDirect("directTest1", "info", "999");
        mqUtils.sendMsgForDirect("directTest1", "warn", "999");
        mqUtils.receiveMsgForDirect("directTest1", new String[]{"error"});
    }
}