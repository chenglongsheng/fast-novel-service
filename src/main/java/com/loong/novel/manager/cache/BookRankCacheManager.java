package com.loong.novel.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.loong.novel.core.constant.CacheConsts;
import com.loong.novel.core.constant.DatabaseConsts;
import com.loong.novel.dao.entity.BookInfo;
import com.loong.novel.dao.mapper.BookInfoMapper;
import com.loong.novel.dto.resp.BookRankRespDto;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 小说排行榜 缓存管理类
 *
 * @author rosen
 * @date 2023/5/21 16:56
 */
@Component
public class BookRankCacheManager {

    @Resource
    private BookInfoMapper bookInfoMapper;

    /**
     * 查询小说点击榜列表，并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER, value = CacheConsts.BOOK_VISIT_RANK_CACHE_NAME)
    public List<BookRankRespDto> listVisitRankBooks() {
        QueryWrapper<BookInfo> bookInfoQueryWrapper = new QueryWrapper<>();
        bookInfoQueryWrapper.orderByDesc(DatabaseConsts.BookTable.COLUMN_VISIT_COUNT);
        return listRankBooks(bookInfoQueryWrapper);
    }

    /**
     * 查询小说新书榜列表，并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER, value = CacheConsts.BOOK_NEWEST_RANK_CACHE_NAME)
    public List<BookRankRespDto> listNewestRankBooks() {
        QueryWrapper<BookInfo> bookInfoQueryWrapper = new QueryWrapper<>();
        bookInfoQueryWrapper
                .gt(DatabaseConsts.BookTable.COLUMN_WORD_COUNT, 0)
                .orderByDesc(DatabaseConsts.CommonColumnEnum.CREATE_TIME.getName());
        return listRankBooks(bookInfoQueryWrapper);
    }

    /**
     * 查询小说更新榜列表，并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER, value = CacheConsts.BOOK_UPDATE_RANK_CACHE_NAME)
    public List<BookRankRespDto> listUpdateRankBooks() {
        QueryWrapper<BookInfo> bookInfoQueryWrapper = new QueryWrapper<>();
        bookInfoQueryWrapper
                .gt(DatabaseConsts.BookTable.COLUMN_WORD_COUNT, 0)
                .orderByDesc(DatabaseConsts.CommonColumnEnum.UPDATE_TIME.getName());
        return listRankBooks(bookInfoQueryWrapper);
    }

    private List<BookRankRespDto> listRankBooks(QueryWrapper<BookInfo> bookInfoQueryWrapper) {
        bookInfoQueryWrapper
                .gt(DatabaseConsts.BookTable.COLUMN_WORD_COUNT, 0)
                .last(DatabaseConsts.SqlEnum.LIMIT_30.getSql());
        return bookInfoMapper.selectList(bookInfoQueryWrapper).stream()
                .map(bookInfo -> {
                    BookRankRespDto bookRankRespDto = new BookRankRespDto();
                    BeanUtils.copyProperties(bookInfo, bookRankRespDto);
                    return bookRankRespDto;
                })
                .toList();
    }

}
