package cn.leafw.blog.springboot.thread.thread;

import cn.leafw.blog.springboot.thread.common.MyAsyncConfigurer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

/**
 * @author CareyWYR
 * @description （用一句话描述这个类的作用）
 * @date 15:44
 */
public class ThreadExecutor {

    private MyAsyncConfigurer myAsyncConfigurer;

    public void DataExecutor(){
        //处理数据
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(100);
        //消费线程数
        int customerThreads = 5;
        //异常收集
        List<Future> futureList = new ArrayList<>();
        //等待customerThreads个线程执行结束后才会结束
        CountDownLatch countDownLatch = new CountDownLatch(customerThreads);

        ThreadDataProvider threadDataProvider = new ThreadDataProvider(queue,countDownLatch);

        myAsyncConfigurer.getAsyncExecutor().execute(threadDataProvider);

    }

    @Async
    public Future<String> asyncInvokeReturnFuture(int i) throws InterruptedException {
        // Future接收返回值，这里是String类型，可以指明其他类型
        Future<String> future = new AsyncResult<String>("success:" + i);
        return future;
    }
}
