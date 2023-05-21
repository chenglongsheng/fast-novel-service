package com.loong.novel.service.impl;

import com.loong.novel.core.common.resp.RestResp;
import com.loong.novel.dao.entity.AuthorInfo;
import com.loong.novel.dao.mapper.AuthorInfoMapper;
import com.loong.novel.dto.AuthorInfoDto;
import com.loong.novel.dto.req.AuthorRegisterReqDto;
import com.loong.novel.manager.cache.AuthorInfoCacheManager;
import com.loong.novel.service.IAuthorService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author rosen
 * @date 2023/5/21 22:19
 */
@Service
public class AuthorServiceImpl implements IAuthorService {

    @Autowired
    private AuthorInfoCacheManager authorInfoCacheManager;

    @Resource
    private AuthorInfoMapper authorInfoMapper;

    @Override
    public RestResp<Integer> getStatus(Long userId) {
        AuthorInfoDto author = authorInfoCacheManager.getAuthor(userId);
        return Objects.isNull(author) ? RestResp.ok(null) : RestResp.ok(author.getStatus());
    }

    @Override
    public RestResp<Void> register(AuthorRegisterReqDto dto) {
        // 校验该用户是否已注册为作家
        AuthorInfoDto author = authorInfoCacheManager.getAuthor(dto.getUserId());
        if (Objects.nonNull(author)) {
            // 该用户已经是作家，直接返回
            return RestResp.ok();
        }
        // 保存作家注册信息
        AuthorInfo authorInfo = new AuthorInfo();
        authorInfo.setUserId(dto.getUserId());
        authorInfo.setChatAccount(dto.getChatAccount());
        authorInfo.setEmail(dto.getEmail());
        authorInfo.setInviteCode("0");
        authorInfo.setTelPhone(dto.getTelPhone());
        authorInfo.setPenName(dto.getPenName());
        authorInfo.setWorkDirection(dto.getWorkDirection());
        authorInfo.setCreateTime(LocalDateTime.now());
        authorInfo.setUpdateTime(LocalDateTime.now());
        authorInfoMapper.insert(authorInfo);
        // 清除作家缓存
        authorInfoCacheManager.evictAuthorCache();
        return RestResp.ok();
    }
}
