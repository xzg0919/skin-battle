package com.skin.api;


import com.skin.core.service.TencentOssService;
import com.skin.params.UtilBean;
import com.taobao.api.ApiException;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.UUID;


@ApiService
public class UtilApi {

    @Autowired
    TencentOssService tencentOssService;

    
    @SneakyThrows
    @Api(name = "util.uploadImage", version = "1.0")
    @SignIgnore
    @AuthIgnore 
    public Object uploadImage(UtilBean utilBean){
        // 获取文件名
        String fileName = utilBean.getFile().getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        try {
            File file = File.createTempFile(uuid, prefix);
            utilBean.getFile().transferTo(file);
            return tencentOssService.upload(file);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException("上传文件失败！");
        }
    }
}
