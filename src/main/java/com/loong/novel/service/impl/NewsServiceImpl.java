package com.loong.novel.service.impl;

import com.loong.novel.core.common.resp.RestResp;
import com.loong.novel.dto.resp.NewsInfoRespDto;
import com.loong.novel.manager.cache.NewsCacheManager;
import com.loong.novel.service.INewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author rosen
 * @date 2023/5/21 17:12
 */
@Service
public class NewsServiceImpl implements INewsService {

    @Autowired
    private NewsCacheManager newsCacheManager;

    @Override
    public RestResp<List<NewsInfoRespDto>> listLatestNews() {
        return RestResp.ok(newsCacheManager.listLatestNews());
    }
}
