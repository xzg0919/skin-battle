package com.skin.config;

import com.tzj.module.easyopen.globle.GlobleConfig;
import com.tzj.module.easyopen.globle.GlobleStartListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author 胡方明（12795880@qq.com）
 **/
@Configuration
public class ApiGlobleStartConfig {
    @Bean
    public GlobleStartListener applicationStartListener(){
        GlobleConfig globleConfig=new GlobleConfig();
        return new GlobleStartListener(globleConfig);
    }

}
