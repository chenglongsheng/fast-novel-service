package com.loong.novel.manager.cache;

import com.loong.novel.dao.mapper.BookInfoMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 小说信息 缓存管理类
 *
 * @author rosen
 * @date 2023/5/21 16:54
 */
@Component
public class BookInfoCacheManager {

    @Resource
    private BookInfoMapper bookInfoMapper;

}
