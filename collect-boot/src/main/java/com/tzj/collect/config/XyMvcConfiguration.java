package com.tzj.collect.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class XyMvcConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private XyInterceptor xyInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //指定拦截器，指定拦截路径
        //registry.addInterceptor(xyInterceptor).addPathPatterns("/xanYu/**");

    }

}
