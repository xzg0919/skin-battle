package com.tzj.green.param;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FilesBean {

    private MultipartFile headImg;

    private String  imgFile;

    private String side;
    private String fileName;
    private String fileContentBase64;
}
