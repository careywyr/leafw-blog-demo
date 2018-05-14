package cn.leafw.etools.threadtest;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author CareyWYR
 * @description
 * @date 2018/5/8 14:51
 */
public class Consumer implements Runnable {
    private ArrayBlockingQueue<Integer> queue = null;

    public Consumer(ArrayBlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(5);
                int result = queue.take();
                System.out.println("当前线程"+Thread.currentThread().getName() + " 取出一条数据，当前队列大小： -> " + queue.size() + ", 取出数据为: " + result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
