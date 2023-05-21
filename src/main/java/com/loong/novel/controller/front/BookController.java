package com.loong.novel.controller.front;

import com.loong.novel.core.common.resp.RestResp;
import com.loong.novel.core.constant.ApiRouterConsts;
import com.loong.novel.dto.resp.BookCategoryRespDto;
import com.loong.novel.dto.resp.BookRankRespDto;
import com.loong.novel.service.IBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
