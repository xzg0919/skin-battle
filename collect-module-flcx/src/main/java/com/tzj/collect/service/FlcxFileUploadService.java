package com.tzj.collect.service;

import com.tzj.module.easyopen.file.FileBase64Param;
import com.tzj.module.easyopen.file.FileBean;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author sgmark
 * @create 2019-07-05 14:01
 **/
public interface FlcxFileUploadService {
    Map<String, Object> upload(HttpServletRequest var1);

    List<FileBean> uploadImage(List<FileBase64Param> var1);

    List<FileBean> uploadImage();

    //不加密上传
    FileBean handleUploadField(String fileName, MultipartFile file);
}
