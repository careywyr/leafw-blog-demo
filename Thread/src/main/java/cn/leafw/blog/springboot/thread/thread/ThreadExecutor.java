package cn.leafw.blog.springboot.thread.thread;

import cn.leafw.blog.springboot.thread.common.MyAsyncConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

/**
 * @author CareyWYR
 * @description （用一句话描述这个类的作用）
 * @date 15:44
 */
public class ThreadExecutor {

    private static Executor executor = new MyAsyncConfigurer().getAsyncExecutor();

    public void DataExecutor() throws Exception{
        //处理数据
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(100);
        //消费线程数
        int customerThreads = 5;
        //生产者只开一个线程
        CountDownLatch countDownLatchProvider = new CountDownLatch(1);
        //消费者开5个线程
        CountDownLatch countDownLatchConsumer = new CountDownLatch(customerThreads);
        //启动生产者
        ThreadDataProvider threadDataProvider = new ThreadDataProvider(queue,countDownLatchProvider);
        executor.execute(threadDataProvider);
        countDownLatchProvider.await();
        //启动消费者
        for (int i = 0; i < 5; i++) {
            ThreadAnalyzeDataConsumer threadAnalyzeDataConsumer = new ThreadAnalyzeDataConsumer(queue,countDownLatchConsumer);
            executor.execute(threadAnalyzeDataConsumer);
        }
        countDownLatchConsumer.await();
    }

}
