package com.tzj.collect.flcx.config;

import com.tzj.module.common.file.upload.FileUpload;
import com.tzj.module.common.file.upload.FileUploadFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author 胡方明（12795880@qq.com）
 **/
@Configuration
public class FileUploadConfig {
    @Bean
    public FileUploadFactoryBean fileUploadFactoryBean(){
        FileUploadFactoryBean fileUploadFactoryBean=new FileUploadFactoryBean();
        fileUploadFactoryBean.setUploadType("oss");
        fileUploadFactoryBean.setAccessId("LTAIMbbuj3E2uX48");
        fileUploadFactoryBean.setAccessKey("V8RPkZqqaBg6QK0mk9GsPcub8ePRyN");
        fileUploadFactoryBean.setBucketName("osssqt");
        fileUploadFactoryBean.setOssEndPointer("http://oss-cn-shanghai-internal.aliyuncs.com");//内网
        //fileUploadFactoryBean.setOssEndPointer("http://oss-cn-shanghai.aliyuncs.com");//外网
        fileUploadFactoryBean.setImageDomain("http://images.sqmall.top");
        return fileUploadFactoryBean;
    }

    @Bean(name="fileUpload")
    public FileUpload fileUpload(FileUploadFactoryBean bean) throws Exception {
        return bean.getObject();
    }
}
