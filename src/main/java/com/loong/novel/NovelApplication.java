package com.loong.novel;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Map;

@SpringBootApplication
@MapperScan("com.loong.novel.dao.mapper")
@EnableCaching
@Slf4j
public class NovelApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovelApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext context) {
        return args -> {
            Map<String, CacheManager> cacheManagerMap = context.getBeansOfType(CacheManager.class);
            log.info("loaded cache manager >>");
            cacheManagerMap.forEach((k, v) -> {
                log.info("{}: {}", k, v.getClass().getName());
                log.info("caches: {}", v.getCacheNames());
            });
            log.info("<<");
        };
    }

}
