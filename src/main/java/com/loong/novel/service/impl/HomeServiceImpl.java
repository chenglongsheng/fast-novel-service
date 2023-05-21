package com.loong.novel.service.impl;

import com.loong.novel.core.common.resp.RestResp;
import com.loong.novel.dto.resp.HomeBookRespDto;
import com.loong.novel.dto.resp.HomeFriendLinkRespDto;
import com.loong.novel.manager.cache.FriendLinkCacheManager;
import com.loong.novel.manager.cache.HomeBookCacheManager;
import com.loong.novel.service.IHomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 首页模块 服务实现类
 *
 * @author rosen
 * @date 2023/5/21 11:33
 */
@Service
public class HomeServiceImpl implements IHomeService {

    @Autowired
    private HomeBookCacheManager homeBookCacheManager;

    @Autowired
    private FriendLinkCacheManager friendLinkCacheManager;

    @Override
    public RestResp<List<HomeBookRespDto>> listHomeBooks() {
        return RestResp.ok(homeBookCacheManager.listHomeBooks());
    }

    @Override
    public RestResp<List<HomeFriendLinkRespDto>> listHomeFriendLinks() {
        return RestResp.ok(friendLinkCacheManager.listHomeFriendLinks());
    }

}
