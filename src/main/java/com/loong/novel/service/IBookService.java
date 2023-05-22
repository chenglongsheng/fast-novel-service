package com.loong.novel.service;

import com.loong.novel.core.common.req.PageReqDto;
import com.loong.novel.core.common.resp.PageRespDto;
import com.loong.novel.core.common.resp.RestResp;
import com.loong.novel.dto.req.BookAddReqDto;
import com.loong.novel.dto.req.ChapterAddReqDto;
import com.loong.novel.dto.req.ChapterUpdateReqDto;
import com.loong.novel.dto.req.UserCommentReqDto;
import com.loong.novel.dto.resp.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @author rosen
 * @date 2023/5/21 16:47
 */
public interface IBookService {
    RestResp<List<BookRankRespDto>> listVisitRankBooks();

    RestResp<List<BookRankRespDto>> listNewestRankBooks();

    RestResp<List<BookRankRespDto>> listUpdateRankBooks();

    RestResp<List<BookCategoryRespDto>> listCategory(Integer workDirection);

    RestResp<PageRespDto<BookInfoRespDto>> listAuthorBooks(PageReqDto dto);

    RestResp<Void> saveBook(BookAddReqDto dto);

    RestResp<PageRespDto<BookChapterRespDto>> listBookChapters(Long bookId, PageReqDto dto);

    RestResp<Void> saveBookChapter(ChapterAddReqDto dto);

    RestResp<ChapterContentRespDto> getBookChapter(Long chapterId);

    RestResp<Void> updateBookChapter(Long chapterId, ChapterUpdateReqDto dto);

    RestResp<Void> deleteBookChapter(Long chapterId);

    RestResp<BookInfoRespDto> getBookById(Long bookId);

    RestResp<List<BookInfoRespDto>> listRecBooks(Long bookId) throws NoSuchAlgorithmException;

    RestResp<BookChapterAboutRespDto> getLastChapterAbout(Long bookId);

    RestResp<BookCommentRespDto> listNewestComments(Long bookId);

    RestResp<BookContentAboutRespDto> getBookContentAbout(Long chapterId);

    RestResp<List<BookChapterRespDto>> listChapters(Long bookId);

    RestResp<Long> getPreChapterId(Long chapterId);

    RestResp<Long> getNextChapterId(Long chapterId);

    RestResp<Void> saveComment(UserCommentReqDto dto);

    RestResp<Void> updateComment(Long userId, Long id, String content);

    RestResp<Void> deleteComment(Long userId, Long id);

    RestResp<PageRespDto<UserCommentRespDto>> listComments(Long userId, PageReqDto pageReqDto);
}
