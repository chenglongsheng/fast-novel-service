package com.loong.novel.core.auth;

import com.loong.novel.core.common.exception.BusinessException;
import org.springframework.stereotype.Component;

/**
 * 平台后台管理系统 认证授权策略
 *
 * @author rosen
 * @date 2023/5/21 11:14
 */
@Component
public class AdminAuthStrategy implements AuthStrategy {
    @Override
    public void auth(String token, String requestUri) throws BusinessException {
        // TODO 平台后台 token 校验
    }
}
