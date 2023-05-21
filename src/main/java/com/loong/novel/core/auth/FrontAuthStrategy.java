package com.loong.novel.core.auth;

import com.loong.novel.core.common.exception.BusinessException;
import com.loong.novel.core.util.JwtUtils;
import com.loong.novel.manager.cache.UserInfoCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 前台门户系统 认证授权策略
 *
 * @author rosen
 * @date 2023/5/21 11:12
 */
@Component
public class FrontAuthStrategy implements AuthStrategy {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserInfoCacheManager userInfoCacheManager;

    @Override
    public void auth(String token, String requestUri) throws BusinessException {
        // 统一账号认证
        authSSO(jwtUtils, userInfoCacheManager, token);
    }
}
