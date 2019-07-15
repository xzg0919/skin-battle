package com.tzj.collect.api.ali;


import com.tzj.collect.core.param.ali.XcxSourceNumBean;
import com.tzj.collect.core.service.XcxSourceNumService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

@ApiService
public class XcxSourceNumApi {

    @Autowired
    private XcxSourceNumService xcxSourceNumService;


    /**
     * 返回小程序首页标题列表
     * @author 王灿
     * @param
     */
    @Api(name = "xcx.saveXcxSourceNum", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object saveXcxSourceNum(XcxSourceNumBean xcxSourceNumBean) {

       return xcxSourceNumService.saveXcxSourceNum(xcxSourceNumBean.getCode());

    }
}
