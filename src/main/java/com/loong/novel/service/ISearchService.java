package com.loong.novel.service;

import com.loong.novel.core.common.resp.PageRespDto;
import com.loong.novel.core.common.resp.RestResp;
import com.loong.novel.dto.req.BookSearchReqDto;
import com.loong.novel.dto.resp.BookInfoRespDto;

/**
 * @author rosen
 * @date 2023/5/21 21:37
 */
public interface ISearchService {
    RestResp<PageRespDto<BookInfoRespDto>> searchBooks(BookSearchReqDto condition);
}
