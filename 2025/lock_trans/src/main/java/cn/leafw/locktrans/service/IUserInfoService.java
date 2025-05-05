package cn.leafw.locktrans.service;

import cn.leafw.locktrans.entity.UserInfoEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author leafw
 * @since 2025-05-01
 */
public interface IUserInfoService extends IService<UserInfoEntity> {
    UserInfoEntity findByUserId(Integer userId);
    void updateUserScore(UserInfoEntity userInfoEntity);
    void incrScore(Integer userId);
}
