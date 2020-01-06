package com.tzj.green.common.config;

import com.baomidou.mybatisplus.enums.IEnum;
import com.baomidou.mybatisplus.handlers.EnumTypeHandler;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.tzj.green.mapper*")
public class MybatisPlusConfig {
//    /**
//     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
//     */
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }


    /**
     * mybatis-plus分页插件<br>
     * 文档：http://mp.baomidou.com<br>
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setLocalPage(true);// 开启 PageHelper 的支持
        return paginationInterceptor;
    }

    
    @Bean
    public TypeHandlerRegistry specialTypeHandlerRegistry(SqlSessionFactory sqlSessionFactory) {
        TypeHandlerRegistry typeHandlerRegistry = sqlSessionFactory.getConfiguration().getTypeHandlerRegistry();
        typeHandlerRegistry.register(IEnum.class, JdbcType.VARCHAR, EnumTypeHandler.class);
        return typeHandlerRegistry;
    }
  
}
