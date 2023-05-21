package com.loong.novel.service;

import com.loong.novel.core.common.resp.RestResp;
import com.loong.novel.dto.resp.NewsInfoRespDto;

import java.util.List;

/**
 * @author rosen
 * @date 2023/5/21 17:12
 */
public interface INewsService {
    RestResp<List<NewsInfoRespDto>> listLatestNews();
}
