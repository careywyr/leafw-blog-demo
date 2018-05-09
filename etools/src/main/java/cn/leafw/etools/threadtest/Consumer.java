package cn.leafw.etools.threadtest;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author CareyWYR
 * @description
 * @date 2018/5/8 14:51
 */
public class Consumer implements Runnable {
    private ArrayBlockingQueue queue = null;

    public Consumer(ArrayBlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        int result = (int)queue.poll();
        System.out.println("result -> "+result);
    }
}
