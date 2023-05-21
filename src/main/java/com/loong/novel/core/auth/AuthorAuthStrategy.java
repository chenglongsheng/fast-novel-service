package com.loong.novel.core.auth;

import com.loong.novel.core.common.constant.ErrorCodeEnum;
import com.loong.novel.core.common.exception.BusinessException;
import com.loong.novel.core.constant.ApiRouterConsts;
import com.loong.novel.core.util.JwtUtils;
import com.loong.novel.dto.AuthorInfoDto;
import com.loong.novel.manager.cache.AuthorInfoCacheManager;
import com.loong.novel.manager.cache.UserInfoCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author rosen
 * @date 2023/5/21 9:24
 */
@Component
public class AuthorAuthStrategy implements AuthStrategy {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserInfoCacheManager userInfoCacheManager;

    @Autowired
    private AuthorInfoCacheManager authorInfoCacheManager;

    /**
     * 不需要进行作家权限认证的 URI
     */
    private static final List<String> EXCLUDE_URI = List.of(
            ApiRouterConsts.API_AUTHOR_URL_PREFIX + "/register",
            ApiRouterConsts.API_AUTHOR_URL_PREFIX + "/status"
    );

    @Override
    public void auth(String token, String requestUri) throws BusinessException {
        // 统一账号认证
        Long userId = authSSO(jwtUtils, userInfoCacheManager, token);
        if (EXCLUDE_URI.contains(requestUri)) {
            // 该请求不需要进行作家权限认证
            return;
        }
        // 作家权限认证
        AuthorInfoDto authorInfo = authorInfoCacheManager.getAuthor(userId);
        if (Objects.isNull(authorInfo)) {
            // 作家账号不存在，无权访问作家专区
            throw new BusinessException(ErrorCodeEnum.USER_UN_AUTH);
        }

        // 设置作家ID到当前线程
        UserHolder.setAuthorId(authorInfo.getId());
    }

}
