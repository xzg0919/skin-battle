package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.ali.AreaBean;
import com.tzj.collect.core.param.ali.CategoryBean;
import com.tzj.collect.entity.CategoryCity;

import java.util.Map;

public interface CategoryCityService extends IService<CategoryCity> {

    Map<String,Object> getCategoryCityLists(CategoryBean categoryBean);


}
