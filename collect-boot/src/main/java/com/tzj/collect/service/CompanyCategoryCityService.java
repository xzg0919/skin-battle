package com.tzj.collect.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.api.ali.param.CategoryBean;
import com.tzj.collect.api.ali.result.ComCatePrice;
import com.tzj.collect.api.business.result.CategoryResult;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.CompanyCategoryCity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CompanyCategoryCityService extends IService<CompanyCategoryCity> {

    List<CategoryResult> getCityHouseHoldDetail(String parentId, String companyId,String cityId);


    List<ComCatePrice> getOwnnerPriceBycityId(String categoryId,String companyId,String cityId);

    List<ComCatePrice> getOwnnerNoPriceBycityId(String categoryId,String companyId,String cityId);

    List<Category> topListAppByCity(String level,String title,String companyId,String cityId);

    List<ComCatePrice> getOwnnerPriceAppByCity(String categoryId,String companyId,String cityId);

}
