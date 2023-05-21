package com.loong.novel.core.config;

import com.loong.novel.core.constant.ApiRouterConsts;
import com.loong.novel.core.constant.SystemConfigConsts;
import com.loong.novel.core.intercepter.AuthInterceptor;
import com.loong.novel.core.intercepter.FileInterceptor;
import com.loong.novel.core.intercepter.TokenParseInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Web Mvc 相关配置不要加 @EnableWebMvc 注解，否则会导致 jackson 的全局配置失效。
 * 因为 @EnableWebMvc 注解会导致
 * WebMvcAutoConfiguration 自动配置失效
 *
 * @author rosen
 * @date 2023/5/21 16:37
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Autowired
    private FileInterceptor fileInterceptor;

    @Autowired
    private TokenParseInterceptor tokenParseInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 文件访问拦截
        registry.addInterceptor(fileInterceptor)
                .addPathPatterns(SystemConfigConsts.IMAGE_UPLOAD_DIRECTORY + "**")
                .order(1);

        // 权限认证拦截
        registry.addInterceptor(authInterceptor)
                .addPathPatterns(
                        // 拦截会员中心相关请求接口
                        ApiRouterConsts.API_FRONT_USER_URL_PREFIX + "/**",
                        // 拦截作家后台相关请求接口
                        ApiRouterConsts.API_AUTHOR_URL_PREFIX + "/**",
                        // 拦截平台后台相关请求接口
                        ApiRouterConsts.API_ADMIN_URL_PREFIX + "/**"
                )
                .excludePathPatterns(
                        // 放行登录注册相关请求接口
                        ApiRouterConsts.API_FRONT_USER_URL_PREFIX + "/register",
                        ApiRouterConsts.API_FRONT_USER_URL_PREFIX + "/login",
                        ApiRouterConsts.API_ADMIN_URL_PREFIX + "/login"
                )
                .order(2);

        // Token 解析拦截器
        registry.addInterceptor(tokenParseInterceptor)
                .addPathPatterns(
                        //拦截小说内容查询接口，需要解析 token 以判断该用户是否有权阅读该章节（付费章节是否已购买）
                        ApiRouterConsts.API_FRONT_BOOK_URL_PREFIX + "/content/*"
                )
                .order(3);

    }
}
