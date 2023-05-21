package com.loong.novel.service;

import com.loong.novel.core.common.resp.RestResp;
import com.loong.novel.dto.resp.HomeBookRespDto;
import com.loong.novel.dto.resp.HomeFriendLinkRespDto;

import java.util.List;

/**
 * 首页模块 服务类
 *
 * @author rosen
 * @date 2023/5/21 11:33
 */
public interface IHomeService {
    RestResp<List<HomeBookRespDto>> listHomeBooks();

    RestResp<List<HomeFriendLinkRespDto>> listHomeFriendLinks();
}
