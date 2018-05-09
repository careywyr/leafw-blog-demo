package cn.leafw.etools.threadtest;

import java.util.concurrent.*;

/**
 * @author CareyWYR
 * @description
 * @date 2018/5/8 14:55
 */
public class Client {

    private static final ArrayBlockingQueue queue = new ArrayBlockingQueue(10);

    private static CountDownLatch countDownLatch = new CountDownLatch(10);

    public static void main(String[] args) throws Exception{
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2,10,100L, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(2));
        threadPoolExecutor.submit(new Thread(new Producer(queue,countDownLatch)));
        threadPoolExecutor.submit(new Thread(new Consumer(queue)));
        countDownLatch.await();
        threadPoolExecutor.shutdown();

    }
}
