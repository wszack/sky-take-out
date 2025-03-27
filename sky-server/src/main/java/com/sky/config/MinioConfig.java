package com.sky.config;

import com.sky.utils.MinioUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Bean
    public MinioUtil minioUtil() {
        return new MinioUtil();
    }
}
