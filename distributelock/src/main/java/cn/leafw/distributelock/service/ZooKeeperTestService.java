package cn.leafw.distributelock.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * zookeeper方式实现分布式锁
 *
 * @author <a href="mailto:wyr95626@95626.cn">CareyWYR</a>
 * @date 2020/8/25
 */
public class ZooKeeperTestService {

    public static void main(String[] args) {
//        exclusiveLockTest();
        sharedLockTest();
//        optimisticLock();
    }

    private static void optimisticLock(){
        int inventory = 5;
        ZooKeeperLockDemoWatcher optimisticLockDemoWatcher = new ZooKeeperLockDemoWatcher(inventory);
        CountDownLatch countDownLatch = new CountDownLatch(inventory);
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < inventory; i++) {
            executorService.execute(() -> {
                optimisticLockDemoWatcher.optimisticLock(countDownLatch);
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("success");
    }

    private static void exclusiveLockTest(){
        int inventory = 5;
        ZooKeeperLockDemoWatcher exclusiveLockWatcher = new ZooKeeperLockDemoWatcher(inventory);
        CountDownLatch countDownLatch = new CountDownLatch(5);
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < inventory; i++) {
            executorService.execute(() -> {
                exclusiveLockWatcher.exclusiveLock2(countDownLatch);
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("success");
    }

    private static void sharedLockTest(){
        int inventory = 5;
        int readThreads = 3;
        ZooKeeperLockDemoWatcher sharedLockDemoWatcher = new ZooKeeperLockDemoWatcher(inventory);
        CountDownLatch countDownLatch = new CountDownLatch(inventory);
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < inventory; i++) {
            executorService.execute(() -> {
                sharedLockDemoWatcher.sharedLock(countDownLatch, "W");
            });
        }
        for (int i = 0; i < readThreads; i++) {
            executorService.execute(() -> {
                sharedLockDemoWatcher.sharedLock(countDownLatch, "R");
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("success");
    }

}

