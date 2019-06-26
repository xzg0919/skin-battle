package com.tzj.collect.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.BrandCommunity;

import java.util.List;
import java.util.Map;

public interface BrandCommunityService  extends IService<BrandCommunity> {

    @DS("slave")
    List<Map<String,Object>> getBrandCommunityList(String streetId);
    @DS("slave")
    List<Map<String,Object>> getBrandCommunityLists(String streetId);
}
