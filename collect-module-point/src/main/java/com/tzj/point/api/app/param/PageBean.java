package com.tzj.point.api.app.param;


import lombok.Data;

@Data
public class PageBean {

    private Integer pageSize = 10;

    private Integer pageNum = 1;

}
