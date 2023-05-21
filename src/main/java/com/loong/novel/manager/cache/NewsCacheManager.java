package com.loong.novel.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.loong.novel.core.constant.CacheConsts;
import com.loong.novel.core.constant.DatabaseConsts;
import com.loong.novel.dao.entity.NewsInfo;
import com.loong.novel.dao.mapper.NewsInfoMapper;
import com.loong.novel.dto.resp.NewsInfoRespDto;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author rosen
 * @date 2023/5/21 17:16
 */
@Component
public class NewsCacheManager {

    @Resource
    private NewsInfoMapper newsInfoMapper;

    /**
     * 最新新闻列表查询，并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER, value = CacheConsts.LATEST_NEWS_CACHE_NAME)
    public List<NewsInfoRespDto> listLatestNews() {
        // 从新闻信息表中查询出最新发布的两条新闻
        QueryWrapper<NewsInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(DatabaseConsts.CommonColumnEnum.CREATE_TIME.getName())
                .last(DatabaseConsts.SqlEnum.LIMIT_2.getSql());
        return newsInfoMapper.selectList(queryWrapper).stream()
                .map(v -> NewsInfoRespDto.builder()
                        .id(v.getId())
                        .categoryId(v.getCategoryId())
                        .categoryName(v.getCategoryName())
                        .title(v.getTitle())
                        .sourceName(v.getSourceName())
                        .updateTime(v.getUpdateTime())
                        .build())
                .toList();
    }

}
