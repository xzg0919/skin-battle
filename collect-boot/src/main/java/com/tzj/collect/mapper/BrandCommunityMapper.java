package com.tzj.collect.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.BrandCommunity;

import java.util.List;
import java.util.Map;

public interface BrandCommunityMapper extends BaseMapper<BrandCommunity> {

    List<Map<String,Object>> getBrandCommunityList(String streetId);

    List<Map<String,Object>> getBrandCommunityLists(String streetId);
}
