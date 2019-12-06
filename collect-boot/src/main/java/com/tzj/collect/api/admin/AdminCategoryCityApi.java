package com.tzj.collect.api.admin;

import com.tzj.collect.core.param.ali.AreaBean;
import com.tzj.collect.core.param.ali.CategoryBean;
import com.tzj.collect.core.service.AreaService;
import com.tzj.collect.core.service.CategoryCityService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;


@ApiService
public class AdminCategoryCityApi {

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
    /**
     * 根据城市Id和二级分类Id获取对应城市某分类的属性标签
     * @author: 王灿
     * @param
     * @return TokenBean    返回类型
     */
    @Api(name = "admin.getCategoryAttrOptionCityLists", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Map<String,Object> getCategoryAttrOptionCityLists(CategoryBean categoryBean){
        return  categoryCityService.getCategoryAttrOptionCityLists(categoryBean);
    }
    /**
     * 根据城市Id和二级分类Id修改对应的价格（适用五废和家电大件的基准价）
     * @author: 王灿
     * @param
     * @return TokenBean    返回类型
     */
    @Api(name = "admin.updateCategoryPriceByCategoryId", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public String updateCategoryPriceByCategoryId(CategoryBean categoryBean){
        return  categoryCityService.updateCategoryPriceByCategoryId(categoryBean);
    }
    /**
     * 根据城市Id和回收物属性Id修改相关的回收物属性信息
     * @author: 王灿
     * @param
     * @return TokenBean    返回类型
     */
    @Api(name = "admin.updateCategoryAttrOptionByOptionId", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public String updateCategoryAttrOptionByOptionId(CategoryBean categoryBean){
        return  categoryCityService.updateCategoryAttrOptionByOptionId(categoryBean);
    }




}
