package com.loong.novel.controller.front;

import com.loong.novel.core.common.resp.RestResp;
import com.loong.novel.core.constant.ApiRouterConsts;
import com.loong.novel.dto.resp.NewsInfoRespDto;
import com.loong.novel.service.INewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前台门户-新闻模块 API 控制器
 *
 * @author rosen
 * @date 2023/5/21 17:11
 */
@Tag(name = "NewsController", description = "前台门户-新闻模块")
@RestController
@RequestMapping(ApiRouterConsts.API_FRONT_NEWS_URL_PREFIX)
public class NewsController {

    @Autowired
    private INewsService newsService;

    /**
     * 最新新闻列表查询接口
     */
    @Operation(summary = "最新新闻列表查询接口")
    @GetMapping("latest_list")
    public RestResp<List<NewsInfoRespDto>> listLatestNews() {
        return newsService.listLatestNews();
    }

}
