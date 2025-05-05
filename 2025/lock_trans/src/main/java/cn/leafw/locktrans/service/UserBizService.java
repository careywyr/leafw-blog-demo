package cn.leafw.locktrans.service;

import cn.leafw.locktrans.annotations.MyLock;
import cn.leafw.locktrans.entity.UserInfoEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserBizService {

    @Resource
    private IUserInfoService userInfoService;

    @MyLock(key = "updateUserScore_{#userId}")
    @Transactional(rollbackFor = Exception.class)
    public void incrScore(Integer userId) {
        UserInfoEntity userInfoEntity = userInfoService.findByUserId(userId);
        userInfoEntity.setScore(userInfoEntity.getScore() + 1);
        userInfoService.updateUserScore(userInfoEntity);
    }

    @MyLock(key = "updateUserScore_{#userId}")
    @Transactional(rollbackFor = Exception.class)
    public void incrScore2(Integer userId) {
        userInfoService.incrScore(userId);
    }
}
