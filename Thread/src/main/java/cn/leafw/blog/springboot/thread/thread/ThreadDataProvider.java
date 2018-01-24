package cn.leafw.blog.springboot.thread.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * @author CareyWYR
 * @description 多线程处理数据提供方
 * @date 15:05
 */
public class ThreadDataProvider implements Runnable{

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadDataProvider.class);

    private final ArrayBlockingQueue<String> queue;

    private final CountDownLatch countDownLatch;

    public ThreadDataProvider(ArrayBlockingQueue<String> queue, CountDownLatch countDownLatch) {
        this.queue = queue;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            queryData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            countDownLatch.countDown();
        }
    }

    private void queryData() throws Exception{

    }
}
