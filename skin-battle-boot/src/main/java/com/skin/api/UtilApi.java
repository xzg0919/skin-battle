package com.skin.api;


import com.skin.config.EmailUtils;
import com.skin.core.service.TencentOssService;
import com.skin.core.service.VerifyMessageService;
import com.skin.entity.VerifyMessage;
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
    @Autowired
    EmailUtils emailUtils;

    @Autowired
    VerifyMessageService verifyMessageService;
    
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

    @Api(name = "util.sendEmail", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object sendEmail(UtilBean utilBean){
        String title = "97SKINS验证码";
        String verifyCode =(int)((Math.random()*9+1)*100000) + "";
        String content = "<html><h1>验证码：<b>"+verifyCode+"</b>,五分钟内有效</h1>  </html>";

        emailUtils.sendMessage(utilBean.getEmail(), title, content);

        VerifyMessage verifyMessage = new VerifyMessage();
        verifyMessage.setVerifyCode(verifyCode);
        verifyMessage.setTo(utilBean.getEmail());
        verifyMessageService.save(verifyMessage);
        return "success";
    }

}
