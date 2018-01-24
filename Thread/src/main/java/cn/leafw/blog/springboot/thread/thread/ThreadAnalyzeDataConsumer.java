package cn.leafw.blog.springboot.thread.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * @author CareyWYR
 * @description 多线程数据处理消费方
 * @date 15:31
 */
public class ThreadAnalyzeDataConsumer implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadAnalyzeDataConsumer.class);

    private final ArrayBlockingQueue<String> queue;

    private final CountDownLatch countDownLatch;

    public ThreadAnalyzeDataConsumer(ArrayBlockingQueue<String> queue, CountDownLatch countDownLatch) {
        this.queue = queue;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {

    }
}
