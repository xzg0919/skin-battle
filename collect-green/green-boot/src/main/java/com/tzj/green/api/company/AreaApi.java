package com.tzj.green.api.company;


import com.tzj.green.param.AreaBean;
import com.tzj.green.param.CompanyCommunityBean;
import com.tzj.green.service.AreaService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tzj.green.common.content.TokenConst.BUSINESS_API_COMMON_AUTHORITY;

@ApiService
public class AreaApi {

    @Autowired
    private AreaService areaService;

    /**
     *获取省份列表
     */
    @Api(name = "company.getProvinceList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object getProvinceList() {
        return  areaService.getProvinceList();
    }
    /**
     *根据父级Id获取子级区域列表（例获取  市，区,街道）
     */
    @Api(name = "company.getAreaList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Object getAreaList(AreaBean areaBean) {
        return  areaService.getAreaList(areaBean.getParentId());
    }

}
