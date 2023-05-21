package com.loong.novel.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.loong.novel.core.constant.CacheConsts;
import com.loong.novel.core.constant.DatabaseConsts;
import com.loong.novel.dao.entity.HomeFriendLink;
import com.loong.novel.dao.mapper.HomeFriendLinkMapper;
import com.loong.novel.dto.resp.HomeFriendLinkRespDto;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author rosen
 * @date 2023/5/21 11:43
 */
@Component
public class FriendLinkCacheManager {

    @Resource
    private HomeFriendLinkMapper homeFriendLinkMapper;

    /**
     * 友情链接列表查询，并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER, value = CacheConsts.HOME_FRIEND_LINK_CACHE_NAME)
    public List<HomeFriendLinkRespDto> listHomeFriendLinks() {
        // 从友情链接表中查询出友情链接列表
        QueryWrapper<HomeFriendLink> homeFriendLinkQueryWrapper = new QueryWrapper<>();
        homeFriendLinkQueryWrapper
                .eq("is_open", 1)
                .orderByAsc(DatabaseConsts.CommonColumnEnum.SORT.getName());
        return homeFriendLinkMapper.selectList(homeFriendLinkQueryWrapper).stream()
                .map(homeFriendLink -> {
                    HomeFriendLinkRespDto homeFriendLinkRespDto = new HomeFriendLinkRespDto();
                    homeFriendLinkRespDto.setLinkName(homeFriendLink.getLinkName());
                    homeFriendLinkRespDto.setLinkUrl(homeFriendLinkRespDto.getLinkUrl());
                    return homeFriendLinkRespDto;
                })
                .toList();
    }
}
