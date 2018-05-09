package cn.leafw.etools.threadtest;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * @author CareyWYR
 * @description
 * @date 2018/5/8 14:49
 */
public class Producer implements Runnable{

    private ArrayBlockingQueue queue = null;

    private CountDownLatch countDownLatch = null;

    public Producer(ArrayBlockingQueue queue,CountDownLatch countDownLatch) {
        this.queue = queue;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        while (true){
            queue.offer(1);
            System.out.println("size -> "+queue.size());
            countDownLatch.countDown();
        }
    }

}
