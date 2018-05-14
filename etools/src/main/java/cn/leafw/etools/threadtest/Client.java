package cn.leafw.etools.threadtest;

import java.util.concurrent.*;

/**
 * @author CareyWYR
 * @description
 * @date 2018/5/8 14:55
 */
public class Client {

    private static final ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue(5);

    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    private static boolean finishFlag = false;

    public static void main(String[] args) throws Exception{
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,10,1000L, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(2));
        executorService.execute(new Producer(queue,countDownLatch));
        for (int i = 0; i < 5; i++) {
            threadPoolExecutor.execute(new Consumer(queue));
        }
        countDownLatch.await();
        threadPoolExecutor.shutdown();
        executorService.shutdownNow();
        System.out.println(threadPoolExecutor.isShutdown());

    }
}
