package cn.leafw.blog.springboot.thread.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * @author CareyWYR
 * @description 多线程处理数据提供方
 * @date 15:05
 */
public class ThreadDataProvider implements Runnable{

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadDataProvider.class);

    private final ArrayBlockingQueue<String> queue;

    private final CountDownLatch countDownLatch;

    private boolean isRunning = true;

    public ThreadDataProvider(ArrayBlockingQueue<String> queue, CountDownLatch countDownLatch) {
        this.queue = queue;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        LOGGER.info("*********启动生产者线程！**********");
        queryData();
    }

    private void queryData(){
        try {
            while (isRunning){
                UUID uuid = UUID.randomUUID();
                String data = uuid.toString().replaceAll("-","");
                LOGGER.info("生成数据，data={}",data);
                if(!queue.offer(data)){
                    LOGGER.error("数据{}放入失败",data);
                    throw new RuntimeException();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
//            isRunning = false;
            countDownLatch.countDown();
        }
    }
}
