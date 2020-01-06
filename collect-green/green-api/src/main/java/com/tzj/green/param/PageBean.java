package com.tzj.green.param;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PageBean {
    //页数
    private Integer pageNum=1;
    //页面大小
    private Integer pageSize=10;



}
