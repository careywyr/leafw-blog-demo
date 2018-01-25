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
        analyzeData();

    }

    private void analyzeData(){
        boolean isRunning = true;
        int count = 0;
        while(isRunning){
            LOGGER.info("从队列抓取数据！");
            String data = queue.poll();
            if(null != data){
                LOGGER.info("成功获取数据，data={}",data);
                count++;
                LOGGER.info(String.valueOf(count));
            }else{
                isRunning = false;
            }
        }
    }
}
