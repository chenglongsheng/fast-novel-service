package com.loong.novel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.loong.novel.core.auth.UserHolder;
import com.loong.novel.core.common.constant.ErrorCodeEnum;
import com.loong.novel.core.common.req.PageReqDto;
import com.loong.novel.core.common.resp.PageRespDto;
import com.loong.novel.core.common.resp.RestResp;
import com.loong.novel.core.constant.DatabaseConsts;
import com.loong.novel.dao.entity.BookChapter;
import com.loong.novel.dao.entity.BookContent;
import com.loong.novel.dao.entity.BookInfo;
import com.loong.novel.dao.mapper.BookChapterMapper;
import com.loong.novel.dao.mapper.BookContentMapper;
import com.loong.novel.dao.mapper.BookInfoMapper;
import com.loong.novel.dto.AuthorInfoDto;
import com.loong.novel.dto.req.BookAddReqDto;
import com.loong.novel.dto.req.ChapterAddReqDto;
import com.loong.novel.dto.req.ChapterUpdateReqDto;
import com.loong.novel.dto.resp.*;
import com.loong.novel.manager.cache.*;
import com.loong.novel.service.IBookService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    @Resource
    private BookInfoMapper bookInfoMapper;

    @Autowired
    private AuthorInfoCacheManager authorInfoCacheManager;

    @Resource
    private BookChapterMapper bookChapterMapper;

    @Resource
    private BookContentMapper bookContentMapper;

    @Autowired
    private BookInfoCacheManager bookInfoCacheManager;

    @Autowired
    private BookChapterCacheManager bookChapterCacheManager;

    @Autowired
    private BookContentCacheManager bookContentCacheManager;

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

    @Override
    public RestResp<PageRespDto<BookInfoRespDto>> listAuthorBooks(PageReqDto dto) {
        Page<BookInfo> page = new Page<>();
        page.setCurrent(dto.getPageNum());
        page.setSize(dto.getPageSize());
        QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookTable.AUTHOR_ID, UserHolder.getAuthorId())
                .orderByDesc(DatabaseConsts.CommonColumnEnum.CREATE_TIME.getName());
        Page<BookInfo> bookInfoPage = bookInfoMapper.selectPage(page, queryWrapper);
        return RestResp.ok(PageRespDto.of(dto.getPageNum(), dto.getPageSize(), bookInfoPage.getTotal(),
                bookInfoPage.getRecords().stream().map(v -> BookInfoRespDto.builder()
                        .id(v.getId())
                        .bookName(v.getBookName())
                        .picUrl(v.getPicUrl())
                        .categoryName(v.getCategoryName())
                        .wordCount(v.getWordCount())
                        .visitCount(v.getVisitCount())
                        .updateTime(v.getUpdateTime())
                        .build()).toList()));
    }

    @Override
    public RestResp<Void> saveBook(BookAddReqDto dto) {
        // 校验小说名是否已存在
        QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookTable.COLUMN_BOOK_NAME, dto.getBookName());
        if (bookInfoMapper.selectCount(queryWrapper) > 0) {
            return RestResp.fail(ErrorCodeEnum.AUTHOR_BOOK_NAME_EXIST);
        }
        BookInfo bookInfo = new BookInfo();
        // 设置作家信息
        AuthorInfoDto author = authorInfoCacheManager.getAuthor(UserHolder.getUserId());
        bookInfo.setAuthorId(author.getId());
        bookInfo.setAuthorName(author.getPenName());
        // 设置其他信息
        bookInfo.setWorkDirection(dto.getWorkDirection());
        bookInfo.setCategoryId(dto.getCategoryId());
        bookInfo.setCategoryName(dto.getCategoryName());
        bookInfo.setBookName(dto.getBookName());
        bookInfo.setPicUrl(dto.getPicUrl());
        bookInfo.setBookDesc(dto.getBookDesc());
        bookInfo.setIsVip(dto.getIsVip());
        bookInfo.setScore(0);
        bookInfo.setCreateTime(LocalDateTime.now());
        bookInfo.setUpdateTime(LocalDateTime.now());
        // 保存小说信息
        bookInfoMapper.insert(bookInfo);
        return RestResp.ok();
    }

    @Override
    public RestResp<PageRespDto<BookChapterRespDto>> listBookChapters(Long bookId, PageReqDto dto) {
        IPage<BookChapter> page = new Page<>();
        page.setCurrent(dto.getPageNum());
        page.setSize(dto.getPageSize());
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, bookId)
                .orderByDesc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM);
        IPage<BookChapter> bookChapterPage = bookChapterMapper.selectPage(page, queryWrapper);
        return RestResp.ok(PageRespDto.of(dto.getPageNum(), dto.getPageSize(), page.getTotal(),
                bookChapterPage.getRecords().stream().map(v -> BookChapterRespDto.builder()
                        .id(v.getId())
                        .chapterName(v.getChapterName())
                        .chapterUpdateTime(v.getUpdateTime())
                        .isVip(v.getIsVip())
                        .build()).toList()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestResp<Void> saveBookChapter(ChapterAddReqDto dto) {
        // 校验该作品是否属于当前作家
        BookInfo bookInfo = bookInfoMapper.selectById(dto.getBookId());
        if (!Objects.equals(bookInfo.getAuthorId(), UserHolder.getAuthorId())) {
            return RestResp.fail(ErrorCodeEnum.USER_UN_AUTH);
        }
        // 1) 保存章节相关信息到小说章节表
        //  a) 查询最新章节号
        int chapterNum = 0;
        QueryWrapper<BookChapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, dto.getBookId())
                .orderByDesc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        BookChapter bookChapter = bookChapterMapper.selectOne(chapterQueryWrapper);
        if (Objects.nonNull(bookChapter)) {
            chapterNum = bookChapter.getChapterNum() + 1;
        }
        //  b) 设置章节相关信息并保存
        BookChapter newBookChapter = new BookChapter();
        newBookChapter.setBookId(dto.getBookId());
        newBookChapter.setChapterName(dto.getChapterName());
        newBookChapter.setChapterNum(chapterNum);
        newBookChapter.setWordCount(dto.getChapterContent().length());
        newBookChapter.setIsVip(dto.getIsVip());
        newBookChapter.setCreateTime(LocalDateTime.now());
        newBookChapter.setUpdateTime(LocalDateTime.now());
        bookChapterMapper.insert(newBookChapter);

        // 2) 保存章节内容到小说内容表
        BookContent bookContent = new BookContent();
        bookContent.setContent(dto.getChapterContent());
        bookContent.setChapterId(newBookChapter.getId());
        bookContent.setCreateTime(LocalDateTime.now());
        bookContent.setUpdateTime(LocalDateTime.now());
        bookContentMapper.insert(bookContent);

        // 3) 更新小说表最新章节信息和小说总字数信息
        //  a) 更新小说表关于最新章节的信息
        BookInfo newBookInfo = new BookInfo();
        newBookInfo.setId(dto.getBookId());
        newBookInfo.setLastChapterId(newBookChapter.getId());
        newBookInfo.setLastChapterName(newBookChapter.getChapterName());
        newBookInfo.setLastChapterUpdateTime(LocalDateTime.now());
        newBookInfo.setWordCount(bookInfo.getWordCount() + newBookChapter.getWordCount());
        newBookChapter.setUpdateTime(LocalDateTime.now());
        bookInfoMapper.updateById(newBookInfo);
        //  b) 清除小说信息缓存
        bookInfoCacheManager.evictBookInfoCache(dto.getBookId());
        //  c) 发送小说信息更新的 MQ 消息
//        amqpMsgManager.sendBookChangeMsg(dto.getBookId());
        return RestResp.ok();
    }

    @Override
    public RestResp<ChapterContentRespDto> getBookChapter(Long chapterId) {
        BookChapterRespDto chapter = bookChapterCacheManager.getChapter(chapterId);
        String bookContent = bookContentCacheManager.getBookContent(chapterId);
        return RestResp.ok(
                ChapterContentRespDto.builder()
                        .chapterName(chapter.getChapterName())
                        .chapterContent(bookContent)
                        .isVip(chapter.getIsVip())
                        .build());
    }

    @Transactional
    @Override
    public RestResp<Void> updateBookChapter(Long chapterId, ChapterUpdateReqDto dto) {
        // 1.查询章节信息
        BookChapterRespDto chapter = bookChapterCacheManager.getChapter(chapterId);
        // 2.查询小说信息
        BookInfoRespDto bookInfo = bookInfoCacheManager.getBookInfo(chapter.getBookId());
        // 3.更新章节信息
        BookChapter newChapter = new BookChapter();
        newChapter.setId(chapterId);
        newChapter.setChapterName(dto.getChapterName());
        newChapter.setWordCount(dto.getChapterContent().length());
        newChapter.setIsVip(dto.getIsVip());
        newChapter.setUpdateTime(LocalDateTime.now());
        bookChapterMapper.updateById(newChapter);
        // 4.更新章节内容
        BookContent newContent = new BookContent();
        newContent.setContent(dto.getChapterContent());
        newContent.setUpdateTime(LocalDateTime.now());
        QueryWrapper<BookContent> bookContentQueryWrapper = new QueryWrapper<>();
        bookContentQueryWrapper.eq(DatabaseConsts.BookContentTable.COLUMN_CHAPTER_ID, chapterId);
        bookContentMapper.update(newContent, bookContentQueryWrapper);
        // 5.更新小说信息
        BookInfo newBookInfo = new BookInfo();
        newBookInfo.setId(chapter.getBookId());
        newBookInfo.setUpdateTime(LocalDateTime.now());
        newBookInfo.setWordCount(
                bookInfo.getWordCount() - chapter.getChapterWordCount() + dto.getChapterContent().length());
        if (Objects.equals(bookInfo.getLastChapterId(), chapterId)) {
            // 更新最新章节信息
            newBookInfo.setLastChapterName(dto.getChapterName());
            newBookInfo.setLastChapterUpdateTime(LocalDateTime.now());
        }
        bookInfoMapper.updateById(newBookInfo);
        // 6.清理章节信息缓存
        bookChapterCacheManager.evictBookChapterCache(chapterId);
        // 7.清理章节内容缓存
        bookContentCacheManager.evictBookContentCache(chapterId);
        // 8.清理小说信息缓存
        bookInfoCacheManager.evictBookInfoCache(chapter.getBookId());
        // 9.发送小说信息更新的 MQ 消息
//        amqpMsgManager.sendBookChangeMsg(chapter.getBookId());
        return RestResp.ok();
    }

    @Override
    public RestResp<Void> deleteBookChapter(Long chapterId) {
        return null;
    }

}
