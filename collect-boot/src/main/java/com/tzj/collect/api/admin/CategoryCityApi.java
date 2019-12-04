package com.tzj.collect.api.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.core.param.ali.AreaBean;
import com.tzj.collect.core.param.ali.CategoryBean;
import com.tzj.collect.core.service.AreaService;
import com.tzj.collect.core.service.CategoryCityService;
import com.tzj.collect.entity.Area;
import com.tzj.collect.entity.Category;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;


@ApiService
public class CategoryCityApi {

    @Autowired
    private CategoryCityService categoryCityService;
    @Autowired
    private AreaService areaService;


    /**
     * 城市价格系数列表
     * @author: 王灿
     * @param
     * @return TokenBean    返回类型
     */
    @Api(name = "admin.getAreaCityRatioLists", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Map<String,Object> getAreaCityRatioLists(AreaBean areaBean){
        return  areaService.getAreaCityRatioLists(areaBean);
    }
    /**
     * 根据城市Id和类型获取一级和二级分类信息
     * @author: 王灿
     * @param
     * @return TokenBean    返回类型
     */
    @Api(name = "admin.getCategoryCityLists", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Map<String,Object> getCategoryCityLists(CategoryBean categoryBean){
        return  categoryCityService.getCategoryCityLists(categoryBean);
    }




}
