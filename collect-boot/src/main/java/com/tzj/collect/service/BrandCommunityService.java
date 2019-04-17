package com.tzj.collect.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.BrandCommunity;
import com.tzj.collect.entity.Order;

import java.util.List;
import java.util.Map;

public interface BrandCommunityService  extends IService<BrandCommunity> {

    List<Map<String,Object>> getBrandCommunityList(String streetId);

    List<Map<String,Object>> getBrandCommunityLists(String streetId);
}
