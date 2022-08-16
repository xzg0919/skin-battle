package com.skin.core.service.impl;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.skin.core.service.TencentOssService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/15 15:28
 * @Description:
 */
@Component
public class TencentOssServiceImpl implements TencentOssService {

    public static COSClient cosClient;

    static {
        // 1 初始化用户身份信息（secretId, secretKey）。
// SECRETID和SECRETKEY请登录访问管理控制台 https://console.cloud.tencent.com/cam/capi 进行查看和管理
        String secretId = "AKIDCF3cw1qRnumQp0uYy83h5E7k6C2cw1sT";
        String secretKey = "OWO0NULxLVQ7ZmlUxA9uQffPu98sNcbn";
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的地域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
// clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region("ap-shanghai");
        ClientConfig clientConfig = new ClientConfig(region);
// 这里建议设置使用 https 协议
// 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 3 生成 cos 客户端。
        cosClient = new COSClient(cred, clientConfig);
    }


    @Override
    public String upload(File file) {
        //https://97skins-1313091640.cos.ap-shanghai.myqcloud.com/images/test.png
// 指定文件将要存放的存储桶
        String bucketName = "97skins-1313091640";
// 指定文件上传到 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        String key = "images/" + file.getName();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
        cosClient.putObject(putObjectRequest);
        return "https://97skins-1313091640.cos.ap-shanghai.myqcloud.com/" + key;
    }


}
