package cn.leafw.etools.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;

/**
 * @author CareyWYR
 * @description
 * @date 2018/5/8 11:49
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Value("thread-pool.corePoolSize")
    private static int corePoolSize;
    @Value("thread-pool.maxPoolSize")
    private static int maxPoolSize;
    @Value("thread-pool.keepAliveTime")
    private static long keepAliveTime;
    @Value("thread-pool.queueCapacity")
    private static int queueCapacity;
    @Value("thread-pool.threadNamePrefix")
    private static String threadNamePrefix;

    private static ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(100);

    public static ThreadPoolExecutor getExecutor(){
       ExecutorService executorService = Executors.newCachedThreadPool();
       ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,maxPoolSize,keepAliveTime,TimeUnit.MILLISECONDS,queue);
       return threadPoolExecutor;
    }
}
