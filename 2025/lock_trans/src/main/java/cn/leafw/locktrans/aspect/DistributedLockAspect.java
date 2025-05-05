package cn.leafw.locktrans.aspect;

import cn.leafw.locktrans.annotations.MyLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
public class DistributedLockAspect {

    @Autowired
    private RedissonClient redissonClient;

    private final ExpressionParser parser = new SpelExpressionParser();
    private final LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    @Around("@annotation(myLock)")
    public Object around(ProceedingJoinPoint point, MyLock myLock) throws Throwable {
        String lockKey = getLockKey(point, myLock);
        RLock lock = redissonClient.getLock(lockKey);
        
        boolean locked = false;
        try {
            // 尝试获取锁
            locked = lock.tryLock(myLock.waitTime(), myLock.leaseTime(), TimeUnit.SECONDS);
            if (!locked) {
                throw new RuntimeException("获取分布式锁失败");
            }
            log.info("lock success: {}", lockKey);
            
            // 执行业务逻辑
            return point.proceed();
        } finally {
            // 释放锁
            if (locked && lock.isHeldByCurrentThread()) {
                log.info("release lock: {}", lockKey);
                lock.unlock();
            }
        }
    }

    /**
     * 获取锁的key
     */
    private String getLockKey(ProceedingJoinPoint point, MyLock myLock) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Object[] args = point.getArgs();
        String[] paramNames = discoverer.getParameterNames(method);
        
        EvaluationContext context = new StandardEvaluationContext();
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }
        
        String key = myLock.key();
        // 匹配形如 {#xxx} 的 SpEL 表达式
        while (key.contains("{") && key.contains("}")) {
            int start = key.indexOf("{");
            int end = key.indexOf("}");
            if (start > end) {
                throw new RuntimeException("锁的key表达式格式错误");
            }
            String spel = key.substring(start + 1, end);
            Expression expression = parser.parseExpression(spel);
            String value = expression.getValue(context, String.class);
            key = key.substring(0, start) + value + key.substring(end + 1);
        }
        return myLock.prefix() + key;
    }
}
