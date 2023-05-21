package com.loong.novel.service.impl;

import com.loong.novel.core.common.resp.RestResp;
import com.loong.novel.dto.resp.BookCategoryRespDto;
import com.loong.novel.dto.resp.BookRankRespDto;
import com.loong.novel.manager.cache.BookCategoryCacheManager;
import com.loong.novel.manager.cache.BookRankCacheManager;
import com.loong.novel.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author rosen
 * @date 2023/5/21 16:47
 */
@Service
public class BookServiceImpl implements IBookService {

    @Autowired
    private BookRankCacheManager bookRankCacheManager;

    @Autowired
    private BookCategoryCacheManager bookCategoryCacheManager;

    @Override
    public RestResp<List<BookRankRespDto>> listVisitRankBooks() {
        return RestResp.ok(bookRankCacheManager.listVisitRankBooks());
    }

    @Override
    public RestResp<List<BookRankRespDto>> listNewestRankBooks() {
        return RestResp.ok(bookRankCacheManager.listNewestRankBooks());
    }

    @Override
    public RestResp<List<BookRankRespDto>> listUpdateRankBooks() {
        return RestResp.ok(bookRankCacheManager.listUpdateRankBooks());
    }

    @Override
    public RestResp<List<BookCategoryRespDto>> listCategory(Integer workDirection) {
        return RestResp.ok(bookCategoryCacheManager.listCategory(workDirection));
    }

}
