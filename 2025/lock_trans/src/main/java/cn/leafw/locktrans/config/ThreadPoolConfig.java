package cn.leafw.locktrans.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池配置
 * @author leafw
 * @since 2025-05-05
 */
@Configuration
public class ThreadPoolConfig {
    
    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        int cpuCores = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(
            cpuCores ,
            cpuCores * 2,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
