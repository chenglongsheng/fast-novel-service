package com.loong.novel.controller.author;

import com.loong.novel.core.auth.UserHolder;
import com.loong.novel.core.common.req.PageReqDto;
import com.loong.novel.core.common.resp.PageRespDto;
import com.loong.novel.core.common.resp.RestResp;
import com.loong.novel.core.constant.ApiRouterConsts;
import com.loong.novel.core.constant.SystemConfigConsts;
import com.loong.novel.dto.req.AuthorRegisterReqDto;
import com.loong.novel.dto.req.BookAddReqDto;
import com.loong.novel.dto.req.ChapterAddReqDto;
import com.loong.novel.dto.req.ChapterUpdateReqDto;
import com.loong.novel.dto.resp.BookChapterRespDto;
import com.loong.novel.dto.resp.BookInfoRespDto;
import com.loong.novel.dto.resp.ChapterContentRespDto;
import com.loong.novel.service.IAuthorService;
import com.loong.novel.service.IBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 作家后台-作家模块 API 控制器
 *
 * @author rosen
 * @date 2023/5/21 11:31
 */
@Tag(name = "AuthorController", description = "作家后台-作者模块")
@SecurityRequirement(name = SystemConfigConsts.HTTP_AUTH_HEADER_NAME)
@RestController
@RequestMapping(ApiRouterConsts.API_AUTHOR_URL_PREFIX)
@RequiredArgsConstructor
public class AuthorController {

    @Autowired
    private IAuthorService authorService;

    @Autowired
    private IBookService bookService;

    /**
     * 查询作家状态接口
     */
    @Operation(summary = "作家状态查询接口")
    @GetMapping("status")
    public RestResp<Integer> getStatus() {
        return authorService.getStatus(UserHolder.getUserId());
    }

    /**
     * 作家注册接口
     */
    @Operation(summary = "作家注册接口")
    @PostMapping("register")
    public RestResp<Void> register(@Valid @RequestBody AuthorRegisterReqDto dto) {
        dto.setUserId(UserHolder.getUserId());
        return authorService.register(dto);
    }

    /**
     * 小说发布列表查询接口
     */
    @Operation(summary = "小说发布列表查询接口")
    @GetMapping("books")
    public RestResp<PageRespDto<BookInfoRespDto>> listBooks(@ParameterObject PageReqDto dto) {
        return bookService.listAuthorBooks(dto);
    }

    /**
     * 小说发布接口
     */
    @Operation(summary = "小说发布接口")
    @PostMapping("book")
    public RestResp<Void> publishBook(@Valid @RequestBody BookAddReqDto dto) {
        return bookService.saveBook(dto);
    }

    /**
     * 小说章节发布列表查询接口
     */
    @Operation(summary = "小说章节发布列表查询接口")
    @GetMapping("book/chapters/{bookId}")
    public RestResp<PageRespDto<BookChapterRespDto>> listBookChapters(
            @Parameter(description = "小说ID") @PathVariable("bookId") Long bookId,
            @ParameterObject PageReqDto dto) {
        return bookService.listBookChapters(bookId, dto);
    }

    /**
     * 小说章节发布接口
     */
    @Operation(summary = "小说章节发布接口")
    @PostMapping("book/chapter/{bookId}")
    public RestResp<Void> publishBookChapter(
            @Parameter(description = "小说ID") @PathVariable("bookId") Long bookId,
            @Valid @RequestBody ChapterAddReqDto dto) {
        dto.setBookId(bookId);
        return bookService.saveBookChapter(dto);
    }

    /**
     * 小说章节查询接口
     */
    @Operation(summary = "小说章节查询接口")
    @GetMapping("book/chapter/{chapterId}")
    public RestResp<ChapterContentRespDto> getBookChapter(
            @Parameter(description = "章节ID") @PathVariable("chapterId") Long chapterId) {
        return bookService.getBookChapter(chapterId);
    }

    /**
     * 小说章节更新接口
     */
    @Operation(summary = "小说章节更新接口")
    @PutMapping("book/chapter/{chapterId}")
    public RestResp<Void> updateBookChapter(
            @Parameter(description = "章节ID") @PathVariable("chapterId") Long chapterId,
            @Valid @RequestBody ChapterUpdateReqDto dto) {
        return bookService.updateBookChapter(chapterId, dto);
    }

    /**
     * 小说章节删除接口
     */
    @Operation(summary = "小说章节删除接口")
    @DeleteMapping("book/chapter/{chapterId}")
    public RestResp<Void> deleteBookChapter(
            @Parameter(description = "章节ID") @PathVariable("chapterId") Long chapterId) {
        return bookService.deleteBookChapter(chapterId);
    }

}
