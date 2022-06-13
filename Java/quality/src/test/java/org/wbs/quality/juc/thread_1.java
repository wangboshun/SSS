package org.wbs.quality.juc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.wbs.quality.ApplicationMain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author WBS
 * Date:2022/6/12
 */

@Slf4j(topic = "thread_1")
@SpringBootTest(classes = ApplicationMain.class)
public class thread_1 {

    @Test
    public void test1() {

        Thread t = new Thread("thread_11111") {

            @Override
            public void run() {
                File f = new File("D://echarts-master.zip");
                try {
                    FileReader reader = new FileReader(f);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

                log.info("read end");
            }

        };

        t.start();
        log.info("test1 end");
    }

    @Test
    public void test2() throws ExecutionException, InterruptedException {

        FutureTask<Integer> f=new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
//                log.info("future task");
                return 123;
            }
        });

        Thread t=new Thread(f,"test2");
        t.start();

        int a=1;

      log.info(""+f.get());
    }
}
