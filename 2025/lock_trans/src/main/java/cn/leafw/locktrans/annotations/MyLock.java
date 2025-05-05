package cn.leafw.locktrans.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式锁注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyLock {
    /**
     * 锁的key前缀
     */
    String prefix() default "mylock:";
    
    /**
     * 锁的key，支持SpEL表达式，使用 {} 包裹 SpEL 表达式
     */
    String key();
    
    /**
     * 等待获取锁的时间，单位：秒
     */
    long waitTime() default 10L;
    
    /**
     * 锁的过期时间，单位：秒
     */
    long leaseTime() default 30L;
}
