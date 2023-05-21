package com.loong.novel.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.loong.novel.core.common.resp.PageRespDto;
import com.loong.novel.core.common.resp.RestResp;
import com.loong.novel.dao.entity.BookInfo;
import com.loong.novel.dao.mapper.BookInfoMapper;
import com.loong.novel.dto.req.BookSearchReqDto;
import com.loong.novel.dto.resp.BookInfoRespDto;
import com.loong.novel.service.ISearchService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author rosen
 * @date 2023/5/21 21:37
 */
@Service
@Slf4j
public class DbSearchServiceImpl implements ISearchService {

    @Resource
    private BookInfoMapper bookInfoMapper;

    @Override
    public RestResp<PageRespDto<BookInfoRespDto>> searchBooks(BookSearchReqDto condition) {
        Page<BookInfoRespDto> page = new Page<>();
        page.setCurrent(condition.getPageNum());
        page.setSize(condition.getPageSize());
        List<BookInfo> bookInfos = bookInfoMapper.searchBooks(page, condition);
        return RestResp.ok(
                PageRespDto.of(
                        condition.getPageNum(), condition.getPageSize(), page.getTotal(),
                        bookInfos.stream()
                                .map(bookInfo -> BookInfoRespDto
                                        .builder()
                                        .id(bookInfo.getId())
                                        .bookName(bookInfo.getBookName())
                                        .categoryId(bookInfo.getCategoryId())
                                        .categoryName(bookInfo.getCategoryName())
                                        .authorId(bookInfo.getAuthorId())
                                        .authorName(bookInfo.getAuthorName())
                                        .wordCount(bookInfo.getWordCount())
                                        .lastChapterName(bookInfo.getLastChapterName())
                                        .build()
                                )
                                .toList()
                )
        );
    }
}
