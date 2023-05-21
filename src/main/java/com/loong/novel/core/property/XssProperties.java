package com.loong.novel.core.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author rosen
 * @date 2023/5/21 0:34
 */
@ConfigurationProperties(prefix = "novel.xss")
public record XssProperties(Boolean enabled, List<String> excludes) {
}
