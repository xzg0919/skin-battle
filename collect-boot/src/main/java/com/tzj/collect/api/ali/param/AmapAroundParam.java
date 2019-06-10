package com.tzj.collect.api.ali.param;

import com.tzj.module.easyopen.doc.annotation.ApiDocField;
import lombok.Data;

import java.io.Serializable;

@Data
public class AmapAroundParam implements Serializable {

    private static final long serialVersionUID = 6981800187882832233L;

    @ApiDocField(description = "经纬度",required=true)
    private String location;

    @ApiDocField(description = "搜索关键字，可以为空",required = false)
    private String searchKey;

    @ApiDocField(description = "每页条数",required = true)
    private String offset;

    @ApiDocField(description = "第几页",required = true)
    private String page;
}
