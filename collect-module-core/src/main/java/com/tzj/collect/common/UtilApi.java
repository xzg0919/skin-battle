package com.tzj.collect.common;


import com.alibaba.fastjson.JSONObject;
import com.tzj.collect.core.param.ali.PageBean;
import com.tzj.collect.core.service.AliPayService;
import com.tzj.collect.core.service.RecyclersService;
import com.tzj.collect.core.service.impl.FileUploadServiceImpl;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;
import com.tzj.module.common.file.upload.FileUpload;
import com.tzj.module.easyopen.file.FileBase64Param;
import com.tzj.module.easyopen.file.FileBean;
import com.tzj.module.easyopen.file.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 工具api
 * @Author 王美霞20180305
 **/
@ApiService
public class UtilApi {

	@Autowired
	private FileUploadService fileUploadService;
	@Autowired
	private FileUploadServiceImpl fileUploadServiceImpl;
	@Autowired
    private FileUpload fileUpload;
    @Autowired
	private AliPayService aliPayService;
    @Autowired
    private RecyclersService recyclersService;
	
    /**
     * 上传文件，客户端只支持 FormData 格式协议
     * 同时这个接口不支持签名验证！！！一定要加 @SignIgnore ，否则会报错
     *
     * 公用接口，不再设置权限
     * @return
     */
    @Api(name = "util.upload", version = "1.0")
    @SignIgnore //这个api忽略sign验证以及随机数以及时间戳验证
    @AuthIgnore
    //@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public List<FileBean> upload(List<FileBean> files){
    	//files 由 FileUploadService 这个实现自动注入进来
    	return files;
    }
    
    @Api(name = "util.uploadImageTwo", version = "1.0")
    @SignIgnore //这个api忽略sign验证以及随机数以及时间戳验证
    @AuthIgnore 
    //@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object uploadImageTwo(PageBean param){
        System.out.println("----------------------开始上传图片了------------------");
        return fileUploadServiceImpl.handleUploadField("", param.getHeadImg());
    }
    
    /**
     * 上传文件0
     */
    @Api(name = "util.uploadImage", version = "1.0")
    @SignIgnore //这个api忽略sign验证以及随机数以及时间戳验证
    @AuthIgnore
    public List<FileBean> uploadImage(FileBase64Param file){
        List<FileBase64Param> files=new ArrayList<>();
        files.add(file);
    	return fileUploadService.uploadImage(files);
    }
//    /**
//     * 上传身份证图片
//     */
//    @Api(name = "util.aliUploadImage", version = "1.0")
//    @SignIgnore //这个api忽略sign验证以及随机数以及时间戳验证
//    @RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
//    public Object aliUploadImage(PageBean param){
//        Recyclers recycler = RecyclersUtils.getRecycler();
//        List<FileBase64Param> files=new ArrayList<>();
//        FileBase64Param file = new FileBase64Param();
//        file.setFileName(param.getFileName());
//        file.setFileContentBase64(param.getFileContentBase64());
//        files.add(file);
//        //将图片传入本地图片服务器
//        List<FileBean> fileBeans = fileUploadService.uploadImage(files);
//        //将身份证图片调用阿里接口解析数据
//        Map<String, Object> resultMap = fileUploadServiceImpl.aliUploadImage(param.getFileContentBase64(), param.getFileName());
//        if(200!=(int)resultMap.get("stat")){
//            //身份信息解析失败
//            return resultMap;
//        }
//        if ("face".equals(param.getFileName())){
//            //身份信息解析成功并且是正面
//            Object getBody = resultMap.get("getBody");
//            Map<String,Object> map =  (Map<String,Object>) JSONObject.parse(getBody.toString());
//            String name = map.get("name")+"";
//            String num = map.get("num")+"";
//            if(StringUtils.isNotBlank(name)&&StringUtils.isNotBlank(num)){
//                //调用芝麻认知初始化接口
//                ZhimaCustomerCertificationInitializeResponse initialize = aliPayService.initialize(name, num);
//                //芝麻认证初返回URL
//                ZhimaCustomerCertificationCertifyResponse getInitializeUrl = aliPayService.getInitializeUrl(initialize.getBizNo());
//                recycler.setBizNo(initialize.getBizNo());
//                recyclersService.updateById(recycler) ;
//                resultMap.put("aliUrl",getInitializeUrl.getBody());
//            }
//        }
//        resultMap.put("fileBeans",fileBeans);
//        return resultMap;
//    }

    public static void main(String[] args) {
        String s = "{\"config_str\":\"{\\\"side\\\":\\\"face\\\"}\",\"address\":\"江苏省沭阳县沭城镇老张圩村郑庄组341号\",\"nationality\":\"汉\",\"success\":true,\"num\":\"321322198805091250\",\"sex\":\"男\",\"name\":\"郑东东\",\"birth\":\"19880509\",\"request_id\":\"20181107142122_6755af85728381ea800619c115fe703e\",\"face_rect\":{\"size\":{\"width\":193.99996948242188,\"height\":193.99996948242188},\"center\":{\"x\":971,\"y\":367},\"angle\":-90},\"face_rect_vertices\":[{\"x\":1068,\"y\":464},{\"x\":874,\"y\":464},{\"x\":874,\"y\":270},{\"x\":1068,\"y\":270}]}";
        Map<String,Object> map =  (Map<String,Object>) JSONObject.parse(s);
        System.out.println(map.get("name"));
        System.out.println(map.get("num"));
    }
}
