package cn.leafw.blog.springboot.thread.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * @author CareyWYR
 * @description 异常处理
 * @date 14:38
 */
public class MyAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyAsyncExceptionHandler.class);

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
        LOGGER.error("捕获异常线程信息，Exception:{},method:{}",throwable.getMessage(),method.getName());
        for (Object object : objects) {
            LOGGER.error("param value -> {}",object );
        }
    }
}
