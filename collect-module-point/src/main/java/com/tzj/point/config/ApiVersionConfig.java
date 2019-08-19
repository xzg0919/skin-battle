package com.tzj.point.config;


import com.baomidou.mybatisplus.plugins.OptimisticLockerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 版本管理
 *
 * @author sgmark
 * @create 2019-05-05 14:54
 **/
@Configuration
public class ApiVersionConfig {
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }
}
