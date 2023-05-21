package com.loong.novel.service;

import com.loong.novel.core.common.resp.RestResp;
import com.loong.novel.dto.req.AuthorRegisterReqDto;

/**
 * @author rosen
 * @date 2023/5/21 22:18
 */
public interface IAuthorService {
    RestResp<Integer> getStatus(Long userId);

    RestResp<Void> register(AuthorRegisterReqDto dto);
}
