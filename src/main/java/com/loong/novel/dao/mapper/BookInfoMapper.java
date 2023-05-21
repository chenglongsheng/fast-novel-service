package com.loong.novel.dao.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.loong.novel.dao.entity.BookInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.loong.novel.dto.req.BookSearchReqDto;
import com.loong.novel.dto.resp.BookInfoRespDto;

import java.util.List;

/**
 * <p>
 * 小说信息 Mapper 接口
 * </p>
 *
 * @author rosen
 * @date 2023/05/21
 */
public interface BookInfoMapper extends BaseMapper<BookInfo> {

    List<BookInfo> searchBooks(Page<BookInfoRespDto> page, BookSearchReqDto condition);
}
