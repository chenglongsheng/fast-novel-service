package com.loong.novel.controller.front;

import com.loong.novel.core.auth.UserHolder;
import com.loong.novel.core.common.req.PageReqDto;
import com.loong.novel.core.common.resp.PageRespDto;
import com.loong.novel.core.common.resp.RestResp;
import com.loong.novel.core.constant.ApiRouterConsts;
import com.loong.novel.core.constant.SystemConfigConsts;
import com.loong.novel.dto.req.UserCommentReqDto;
import com.loong.novel.dto.req.UserInfoUptReqDto;
import com.loong.novel.dto.req.UserLoginReqDto;
import com.loong.novel.dto.req.UserRegisterReqDto;
import com.loong.novel.dto.resp.UserCommentRespDto;
import com.loong.novel.dto.resp.UserInfoRespDto;
import com.loong.novel.dto.resp.UserLoginRespDto;
import com.loong.novel.dto.resp.UserRegisterRespDto;
import com.loong.novel.service.IBookService;
import com.loong.novel.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 前台门户-会员模块 API 控制器
 *
 * @author rosen
 * @date 2023/5/21 19:24
 */
@Tag(name = "UserController", description = "前台门户-会员模块")
@SecurityRequirement(name = SystemConfigConsts.HTTP_AUTH_HEADER_NAME)
@RestController
@RequestMapping(ApiRouterConsts.API_FRONT_USER_URL_PREFIX)
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IBookService bookService;

    /**
     * 用户注册接口
     */
    @Operation(summary = "用户注册接口")
    @PostMapping("register")
    public RestResp<UserRegisterRespDto> register(@Valid @RequestBody UserRegisterReqDto dto) {
        return userService.register(dto);
    }

    /**
     * 用户登录接口
     */
    @Operation(summary = "用户登录接口")
    @PostMapping("login")
    public RestResp<UserLoginRespDto> login(@Valid @RequestBody UserLoginReqDto dto) {
        return userService.login(dto);
    }

    /**
     * 用户信息查询接口
     */
    @Operation(summary = "用户信息查询接口")
    @GetMapping
    public RestResp<UserInfoRespDto> getUserInfo() {
        return userService.getUserInfo(UserHolder.getUserId());
    }

    /**
     * 用户信息修改接口
     */
    @Operation(summary = "用户信息修改接口")
    @PutMapping
    public RestResp<Void> updateUserInfo(@Valid @RequestBody UserInfoUptReqDto dto) {
        dto.setUserId(UserHolder.getUserId());
        return userService.updateUserInfo(dto);
    }

    /**
     * 用户反馈提交接口
     */
    @Operation(summary = "用户反馈提交接口")
    @PostMapping("feedback")
    public RestResp<Void> submitFeedback(@RequestBody String content) {
        return userService.saveFeedback(UserHolder.getUserId(), content);
    }

    /**
     * 用户反馈删除接口
     */
    @Operation(summary = "用户反馈删除接口")
    @DeleteMapping("feedback/{id}")
    public RestResp<Void> deleteFeedback(@Parameter(description = "反馈ID") @PathVariable Long id) {
        return userService.deleteFeedback(UserHolder.getUserId(), id);
    }

    /**
     * 发表评论接口
     */
    @Operation(summary = "发表评论接口")
    @PostMapping("comment")
    public RestResp<Void> comment(@Valid @RequestBody UserCommentReqDto dto) {
        dto.setUserId(UserHolder.getUserId());
        return bookService.saveComment(dto);
    }

    /**
     * 修改评论接口
     */
    @Operation(summary = "修改评论接口")
    @PutMapping("comment/{id}")
    public RestResp<Void> updateComment(
            @Parameter(description = "评论ID")
            @PathVariable Long id, String content
    ) {
        return bookService.updateComment(UserHolder.getUserId(), id, content);
    }

    /**
     * 删除评论接口
     */
    @Operation(summary = "删除评论接口")
    @DeleteMapping("comment/{id}")
    public RestResp<Void> deleteComment(@Parameter(description = "评论ID") @PathVariable Long id) {
        return bookService.deleteComment(UserHolder.getUserId(), id);
    }

    /**
     * 查询书架状态接口 0-不在书架 1-已在书架
     */
    @Operation(summary = "查询书架状态接口")
    @GetMapping("bookshelf_status")
    public RestResp<Integer> getBookshelfStatus(@Parameter(description = "小说ID") String bookId) {
        return userService.getBookshelfStatus(UserHolder.getUserId(), bookId);
    }

    /**
     * 分页查询评论
     */
    @Operation(summary = "查询会员评论列表接口")
    @GetMapping("comments")
    public RestResp<PageRespDto<UserCommentRespDto>> listComments(PageReqDto pageReqDto) {
        return bookService.listComments(UserHolder.getUserId(), pageReqDto);
    }

}
