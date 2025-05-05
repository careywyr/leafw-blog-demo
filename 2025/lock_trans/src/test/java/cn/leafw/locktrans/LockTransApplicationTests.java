package cn.leafw.locktrans;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.Resource;

import cn.leafw.locktrans.service.IUserInfoService;
import cn.leafw.locktrans.service.UserBizService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import cn.leafw.locktrans.entity.UserInfoEntity;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class LockTransApplicationTests {

    @Resource
    private UserBizService userBizService;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;
    @Resource
    private IUserInfoService userInfoService;

    @Test
    void contextLoads() {
    }

    @Test
    void userTest1() {
        int loopCount = 1000;
        CountDownLatch countDownLatch = new CountDownLatch(loopCount);
        
        for (int i = 0; i < loopCount; i++) {
            threadPoolExecutor.execute(() -> {
                try {
                    userBizService.incrScore(1);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        ending(countDownLatch);

    }

    @Test
    void userTest2() {
        int loopCount = 10000;
        CountDownLatch countDownLatch = new CountDownLatch(loopCount);

        for (int i = 0; i < loopCount; i++) {
            threadPoolExecutor.execute(() -> {
                userBizService.incrScore2(1);
                countDownLatch.countDown();
            });
        }

        ending(countDownLatch);
    }

    private void ending(CountDownLatch countDownLatch) {
        try {
            countDownLatch.await();
            log.info("All threads completed");
            UserInfoEntity finalResult = userInfoService.findByUserId(1);
            log.info("Final score: {}", finalResult.getScore());
        } catch (InterruptedException e) {
            log.error("Test interrupted", e);
        }
    }

}
