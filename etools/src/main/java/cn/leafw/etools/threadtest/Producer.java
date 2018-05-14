package cn.leafw.etools.threadtest;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author CareyWYR
 * @description
 * @date 2018/5/8 14:49
 */
public class Producer implements Runnable{

    private ArrayBlockingQueue<Integer> queue = null;

    private CountDownLatch countDownLatch = null;

    public Producer(ArrayBlockingQueue<Integer> queue, CountDownLatch countDownLatch) {
        this.queue = queue;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                queue.put(i);
                System.out.println("队列成功插入了一条数据，当前队列大小 " + queue.size());
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        countDownLatch.countDown();
    }

}
