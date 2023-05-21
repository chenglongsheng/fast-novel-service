package com.loong.novel.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.loong.novel.core.constant.CacheConsts;
import com.loong.novel.core.constant.DatabaseConsts;
import com.loong.novel.dao.entity.BookChapter;
import com.loong.novel.dao.entity.BookInfo;
import com.loong.novel.dao.mapper.BookChapterMapper;
import com.loong.novel.dao.mapper.BookInfoMapper;
import com.loong.novel.dto.resp.BookInfoRespDto;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * 小说信息 缓存管理类
 *
 * @author rosen
 * @date 2023/5/21 16:54
 */
@Component
public class BookInfoCacheManager {

    @Resource
    private BookInfoMapper bookInfoMapper;

    @Resource
    private BookChapterMapper bookChapterMapper;

    @CacheEvict(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
            value = CacheConsts.BOOK_INFO_CACHE_NAME)
    public void evictBookInfoCache(Long bookId) {
        // 调用此方法自动清除小说信息的缓存
    }

    /**
     * 从缓存中查询小说信息（先判断缓存中是否已存在，存在则直接从缓存中取，否则执行方法体中的逻辑后缓存结果）
     */
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
            value = CacheConsts.BOOK_INFO_CACHE_NAME)
    public BookInfoRespDto getBookInfo(Long id) {
        // 查询基础信息
        BookInfo bookInfo = bookInfoMapper.selectById(id);
        // 查询首章ID
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, id)
                .orderByAsc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        BookChapter firstBookChapter = bookChapterMapper.selectOne(queryWrapper);
        // 组装响应对象
        return BookInfoRespDto.builder()
                .id(bookInfo.getId())
                .bookName(bookInfo.getBookName())
                .bookDesc(bookInfo.getBookDesc())
                .bookStatus(bookInfo.getBookStatus())
                .authorId(bookInfo.getAuthorId())
                .authorName(bookInfo.getAuthorName())
                .categoryId(bookInfo.getCategoryId())
                .categoryName(bookInfo.getCategoryName())
                .commentCount(bookInfo.getCommentCount())
                .firstChapterId(firstBookChapter.getId())
                .lastChapterId(bookInfo.getLastChapterId())
                .picUrl(bookInfo.getPicUrl())
                .visitCount(bookInfo.getVisitCount())
                .wordCount(bookInfo.getWordCount())
                .build();
    }

}
