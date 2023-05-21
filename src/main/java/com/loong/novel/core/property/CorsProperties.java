package com.loong.novel.core.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author rosen
 * @date 2023/5/21 16:33
 */
@ConfigurationProperties(prefix = "novel.cors")
public record CorsProperties(List<String> allowOrigins) {
}
