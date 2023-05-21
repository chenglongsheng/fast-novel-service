package com.loong.novel.controller.front;

import com.loong.novel.core.common.resp.RestResp;
import com.loong.novel.core.constant.ApiRouterConsts;
import com.loong.novel.dto.resp.*;
import com.loong.novel.service.IBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 前台门户-小说模块 API 控制器
 *
 * @author rosen
 * @date 2023/5/21 16:45
 */
@Tag(name = "BookController", description = "前台门户-小说模块")
@RestController
@RequestMapping(ApiRouterConsts.API_FRONT_BOOK_URL_PREFIX)
public class BookController {

    @Autowired
    private IBookService bookService;

    /**
     * 小说点击榜查询接口
     */
    @Operation(summary = "小说点击榜查询接口")
    @GetMapping("/visit_rank")
    public RestResp<List<BookRankRespDto>> listVisitRankBooks() {
        return bookService.listVisitRankBooks();
    }

    /**
     * 小说新书榜查询接口
     */
    @Operation(summary = "小说新书榜查询接口")
    @GetMapping("/newest_rank")
    public RestResp<List<BookRankRespDto>> listNewestRankBooks() {
        return bookService.listNewestRankBooks();
    }

    /**
     * 小说更新榜查询接口
     */
    @Operation(summary = "小说更新榜查询接口")
    @GetMapping("/update_rank")
    public RestResp<List<BookRankRespDto>> listUpdateRankBooks() {
        return bookService.listUpdateRankBooks();
    }

    /**
     * 小说分类列表查询接口
     */
    @Operation(summary = "小说分类列表查询接口")
    @GetMapping("category/list")
    public RestResp<List<BookCategoryRespDto>> listCategory(
            @Parameter(description = "作品方向", required = true) Integer workDirection) {
        return bookService.listCategory(workDirection);
    }

    /**
     * 小说信息查询接口
     */
    @Operation(summary = "小说信息查询接口")
    @GetMapping("{id}")
    public RestResp<BookInfoRespDto> getBookById(
            @Parameter(description = "小说 ID") @PathVariable("id") Long bookId) {
        return bookService.getBookById(bookId);
    }

    /**
     * 小说推荐列表查询接口
     */
    @Operation(summary = "小说推荐列表查询接口")
    @GetMapping("rec_list")
    public RestResp<List<BookInfoRespDto>> listRecBooks(
            @Parameter(description = "小说ID") Long bookId) throws NoSuchAlgorithmException {
        return bookService.listRecBooks(bookId);
    }

    /**
     * 小说最新章节相关信息查询接口
     */
    @Operation(summary = "小说最新章节相关信息查询接口")
    @GetMapping("last_chapter/about")
    public RestResp<BookChapterAboutRespDto> getLastChapterAbout(
            @Parameter(description = "小说ID") Long bookId) {
        return bookService.getLastChapterAbout(bookId);
    }

    /**
     * 小说最新评论查询接口
     */
    @Operation(summary = "小说最新评论查询接口")
    @GetMapping("comment/newest_list")
    public RestResp<BookCommentRespDto> listNewestComments(
            @Parameter(description = "小说ID") Long bookId) {
        return bookService.listNewestComments(bookId);
    }

}
