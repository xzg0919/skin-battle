package com.tzj.collect.core.param.admin;

import com.tzj.collect.core.param.ali.PageBean;
import lombok.Data;

@Data
public class TransStationBean {

    private String name;

    private String tel;

    private PageBean pageBean;

    private String transStationId;

    private String companyId;

    private String address;

    private String contacts;

    private String longitude;//经度

    private String latitude;//纬度

    private String password;

    private String isEnable; //0启用  1禁用

    private Integer pageSize = 10;

    private Integer pageNum = 1;


}
