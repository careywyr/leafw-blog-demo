package cn.leafw.locktrans.service.impl;

import cn.leafw.locktrans.annotations.MyLock;
import cn.leafw.locktrans.entity.UserInfoEntity;
import cn.leafw.locktrans.mapper.UserInfoMapper;
import cn.leafw.locktrans.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author leafw
 * @since 2025-05-01
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfoEntity> implements IUserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public UserInfoEntity findByUserId(Integer userId) {
        return lambdaQuery()
                    .eq(UserInfoEntity::getUserId, userId)
                    .one();
    }


    @Transactional(rollbackFor = Exception.class)
    public void updateUserScore(UserInfoEntity userInfoEntity) {
        lambdaUpdate()
                .eq(UserInfoEntity::getUserId, userInfoEntity.getUserId())
                .set(UserInfoEntity::getScore, userInfoEntity.getScore())
                .update();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrScore(Integer userId) {
        lambdaUpdate()
                .eq(UserInfoEntity::getUserId, userId)
                .setSql("score = score + 1")
                .update();
    }

}
