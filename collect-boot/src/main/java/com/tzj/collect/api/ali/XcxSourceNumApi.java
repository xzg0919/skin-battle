package com.tzj.collect.api.ali;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.api.ali.param.XcxSourceNumBean;
import com.tzj.collect.entity.XcxSourceNum;
import com.tzj.collect.entity.XcxSourceTitle;
import com.tzj.collect.entity.XcxTheme;
import com.tzj.collect.entity.XcxThemeContent;
import com.tzj.collect.service.XcxSourceNumService;
import com.tzj.collect.service.XcxSourceTitleService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
