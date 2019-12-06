package com.tzj.collect.api.admin;


import com.tzj.collect.core.param.ali.AreaBean;
import com.tzj.collect.core.param.ali.CategoryBean;
import com.tzj.collect.core.service.CompanyCategoryCityService;
import com.tzj.collect.entity.CompanyCategoryCity;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

@ApiService
public class AdminCompanyCategoryCityApi {

    @Autowired
    private CompanyCategoryCityService companyCategoryCityService;

    /**
     * 根据城市id和公司id 和类型获取相关类型的一级分类和二级分类
     * @author: 王灿
     * @param
     * @return TokenBean    返回类型
     */
    @Api(name = "admin.getCompanyCategoryListByCityTitle", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Map<String,Object> getCompanyCategoryListByCityTitle(CategoryBean categoryBean){
        return  companyCategoryCityService.getCompanyCategoryListByCityTitle(categoryBean);
    }
    /**
     * 根据服务商Id 城市Id和二级分类Id获取对应城市某分类的属性标签
     * @author: 王灿
     * @param
     * @return TokenBean    返回类型
     */
    @Api(name = "admin.getCompanyCategoryAttrOptionCityLists", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Map<String,Object> getCompanyCategoryAttrOptionCityLists(CategoryBean categoryBean){
        return  companyCategoryCityService.getCompanyCategoryAttrOptionCityLists(categoryBean);
    }
    /**
     * 根据服务商id,城市Id和二级分类Id修改对应的价格（适用五废和家电大件的基准价）
     * @author: 王灿
     * @param
     * @return TokenBean    返回类型
     */
    @Api(name = "admin.updateCompanyCategoryPriceByCategoryId", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public String updateCompanyCategoryPriceByCategoryId(CategoryBean categoryBean){
        return  companyCategoryCityService.updateCompanyCategoryPriceByCategoryId(categoryBean);
    }
    /**
     * 根据服务商Id,城市Id和回收物属性Id修改相关的回收物属性信息
     * @author: 王灿
     * @param
     * @return TokenBean    返回类型
     */
    @Api(name = "admin.updateCompanyCategoryAttrOptionByOptionId", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public String updateCompanyCategoryAttrOptionByOptionId(CategoryBean categoryBean){
        return  companyCategoryCityService.updateCompanyCategoryAttrOptionByOptionId(categoryBean);
    }


}
