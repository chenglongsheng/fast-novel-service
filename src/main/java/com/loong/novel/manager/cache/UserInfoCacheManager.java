package com.loong.novel.manager.cache;

import com.loong.novel.core.constant.CacheConsts;
import com.loong.novel.dao.entity.UserInfo;
import com.loong.novel.dao.mapper.UserInfoMapper;
import com.loong.novel.dto.UserInfoDto;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 用户信息 缓存管理类
 *
 * @author rosen
 * @date 2023/5/21 9:33
 */
@Component
public class UserInfoCacheManager {

    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 查询用户信息，并放入缓存中
     */
    @Cacheable(
            cacheManager = CacheConsts.REDIS_CACHE_MANAGER,
            value = CacheConsts.USER_INFO_CACHE_NAME
    )
    public UserInfoDto getUser(Long userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        if (Objects.isNull(userInfo)) {
            return null;
        }
        return UserInfoDto.builder()
                .id(userInfo.getId())
                .status(userInfo.getStatus())
                .build();
    }

}
