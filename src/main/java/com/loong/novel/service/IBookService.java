package com.loong.novel.service;

import com.loong.novel.core.common.resp.RestResp;
import com.loong.novel.dto.resp.BookRankRespDto;

import java.util.List;

/**
 * @author rosen
 * @date 2023/5/21 16:47
 */
public interface IBookService {
    RestResp<List<BookRankRespDto>> listVisitRankBooks();

    RestResp<List<BookRankRespDto>> listNewestRankBooks();

    RestResp<List<BookRankRespDto>> listUpdateRankBooks();
}
