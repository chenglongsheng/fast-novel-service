package com.loong.novel.controller.front;

import com.loong.novel.core.common.resp.RestResp;
import com.loong.novel.core.constant.ApiRouterConsts;
import com.loong.novel.dto.resp.HomeBookRespDto;
import com.loong.novel.dto.resp.HomeFriendLinkRespDto;
import com.loong.novel.service.IHomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前台门户-首页模块 API 控制器
 *
 * @author rosen
 * @date 2023/5/21 11:32
 */
@Tag(name = "HomeController", description = "前台门户-首页模块")
@RestController
@RequestMapping(ApiRouterConsts.API_FRONT_HOME_URL_PREFIX)
public class HomeController {

    @Autowired
    private IHomeService homeService;

    /**
     * 首页小说推荐查询接口
     */
    @Operation(summary = "首页小说推荐查询接口")
    @GetMapping("/books")
    public RestResp<List<HomeBookRespDto>> listHomeBooks() {
        return homeService.listHomeBooks();
    }

    /**
     * 首页友情链接列表查询接口
     */
    @Operation(summary = "首页友情链接列表查询接口")
    @GetMapping("/friend_Link/list")
    public RestResp<List<HomeFriendLinkRespDto>> listHomeFriendLinks() {
        return homeService.listHomeFriendLinks();
    }

}
